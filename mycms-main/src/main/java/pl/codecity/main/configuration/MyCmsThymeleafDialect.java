package pl.codecity.main.configuration;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class MyCmsThymeleafDialect extends AbstractDialect implements IExpressionObjectDialect {

	public static final String NAME = "MyCms";

	private MyCmsExpressionObjectFactory myCmsExpressionObjectFactory;

	protected MyCmsThymeleafDialect(MyCmsExpressionObjectFactory myCmsExpressionObjectFactory) {
		super(NAME);
		this.myCmsExpressionObjectFactory = myCmsExpressionObjectFactory;
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return myCmsExpressionObjectFactory;
	}
}
