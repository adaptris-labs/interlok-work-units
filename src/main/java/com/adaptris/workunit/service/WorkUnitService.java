package com.adaptris.workunit.service;

import java.io.InputStream;
import java.net.URL;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMarshaller;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.core.util.LifecycleHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@XStreamAlias("work-unit-service")
@AdapterComponent
@ComponentProfile(summary = "Execute the named work unit.", tag = "service,work-unit")
@DisplayOrder(order = {"workUnitName"})
public class WorkUnitService extends ServiceImp {

  private static final String WORK_UNIT_URL_PREFIX = "workunit:";
  
  private static final String XML_EXTENSION = ".xml";
  
  @Getter
  @Setter
  private String workUnitName;
  
  @Getter(value = AccessLevel.PROTECTED)
  @Setter(value = AccessLevel.PROTECTED)
  private Service proxiedService;
  
  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    getProxiedService().doService(msg);
  }

  private Service deserializeService(InputStream inputStream) throws CoreException {
    AdaptrisMarshaller marshaller = DefaultMarshaller.getDefaultMarshaller();
    return (Service) marshaller.unmarshal(inputStream);
  }
  
  @Override
  public void prepare() throws CoreException {
    try {
      setProxiedService(
          deserializeService(
              new URL(WORK_UNIT_URL_PREFIX + getWorkUnitName() + XML_EXTENSION).openStream()
          )
      );
      LifecycleHelper.prepare(getProxiedService());
    } catch (Exception e) {
      throw ExceptionHelper.wrapCoreException(e);
    } 
  }

  @Override
  public void start() throws CoreException {
    LifecycleHelper.start(getProxiedService());
  }
  
  @Override
  public void stop() {
    LifecycleHelper.stop(getProxiedService());
  }
  @Override
  protected void initService() throws CoreException {
    LifecycleHelper.init(getProxiedService());
  }

  @Override
  protected void closeService() {
    LifecycleHelper.close(getProxiedService());
  }

}
