/*
 * Copyright 2026-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.web;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

/**
 * Validates request-bound sort properties against an optional {@link AllowedSortFields} declaration.
 *
 * @author
 * @since 4.2
 */
final class SortFieldWhitelist {

	private SortFieldWhitelist() {}

	static Sort validate(MethodParameter parameter, Sort sort) {

		AllowedSortFields annotation = parameter.getParameterAnnotation(AllowedSortFields.class);

		if (annotation == null) {
			return sort;
		}

		Assert.noNullElements(annotation.value(), "Allowed sort fields must not contain null");

		Set<String> allowed = Arrays.stream(annotation.value()).collect(Collectors.toUnmodifiableSet());

		for (Sort.Order order : sort) {
			if (!allowed.contains(order.getProperty())) {
				throw new IllegalArgumentException(String.format(
						"Sort property '%s' is not allowed. Allowed sort properties are %s", order.getProperty(), allowed));
			}
		}

		return sort;
	}
}
