package com.example.demo.dto;

//public class ServiceStatusKey {
//
//}

import java.util.Objects;

public class ServiceStatusKey {
	private String serviceName;
	private String status;

	public ServiceStatusKey(String serviceName, String status) {
		this.serviceName = serviceName;
		this.status = status;
	}

	// Getters if needed
	public String getServiceName() {
		return serviceName;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ServiceStatusKey))
			return false;
		ServiceStatusKey that = (ServiceStatusKey) o;
		return serviceName.equals(that.serviceName) && status.equals(that.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceName, status);
	}
}
