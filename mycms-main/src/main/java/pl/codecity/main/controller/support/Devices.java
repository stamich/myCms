package pl.codecity.main.controller.support;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.IWebContext;

public class Devices {

	private IExpressionContext context;

	private DeviceResolver deviceResolver;

	public Devices(IExpressionContext context, DeviceResolver deviceResolver) {
		this.context = context;
		this.deviceResolver = deviceResolver;
	}

	public boolean isMobile() {
		return resolveDevice().isMobile();
	}

	public boolean isNormal() {
		return resolveDevice().isNormal();
	}

	public boolean isTablet() {
		return resolveDevice().isTablet();
	}

	private Device resolveDevice() {
		return deviceResolver.resolveDevice(((IWebContext) context).getRequest());
	}
}
