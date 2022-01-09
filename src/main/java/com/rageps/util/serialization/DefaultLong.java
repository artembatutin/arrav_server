package com.rageps.util.serialization;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultLong {
	
	public long defaultValue();
	
}