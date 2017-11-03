/**
 * Copyright (C) 2010 Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marvinformatics.formatter;

import java.util.function.Function;

public class CacheableFormatter extends AbstractCacheableFormatter implements Formatter {

	private final Function<String, String> doFormat;

	public CacheableFormatter(ConfigurationSource cfg, Function<String, String> doFormat) {
		super(cfg);

		this.doFormat = doFormat;
	}

	@Override
	protected String doFormat(String code) {
		return doFormat.apply(code);
	}

}
