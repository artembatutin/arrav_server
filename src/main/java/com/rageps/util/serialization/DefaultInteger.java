package com.rageps.util.serialization;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface DefaultInteger {
	
	public int defaultValue();
	
}
