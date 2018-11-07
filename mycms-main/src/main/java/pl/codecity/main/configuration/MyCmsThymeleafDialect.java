package pl.codecity.main.configuration;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class MyCmsThymeleafDialect extends AbstractDialect implements IExpressionObjectDialect {

	public static final String NAME = "WallRide";

	private MyCmsExpressionObjectFactory wallRideExpressionObjectFactory;

	protected MyCmsThymeleafDialect(MyCmsExpressionObjectFactory wallRideExpressionObjectFactory) {
		super(NAME);
		this.wallRideExpressionObjectFactory = wallRideExpressionObjectFactory;
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return wallRideExpressionObjectFactory;
	}
}
