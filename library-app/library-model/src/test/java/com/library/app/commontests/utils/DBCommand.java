package com.library.app.commontests.utils;

import org.junit.*;

@Ignore
public interface DBCommand<T> {

	T execute();
}
