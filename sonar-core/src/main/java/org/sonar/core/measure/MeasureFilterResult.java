/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.core.measure;

import javax.annotation.Nullable;

import java.util.List;

public class MeasureFilterResult {

  public static enum Error {
    TOO_MANY_RESULTS, UNKNOWN
  }

  private List<MeasureFilterRow> rows = null;
  private Error error = null;
  private long durationInMs;

  MeasureFilterResult() {
  }

  public List<MeasureFilterRow> getRows() {
    return rows;
  }

  public Error getError() {
    return error;
  }

  public long getDurationInMs() {
    return durationInMs;
  }

  MeasureFilterResult setDurationInMs(long l) {
    this.durationInMs = l;
    return this;
  }

  MeasureFilterResult setRows(@Nullable List<MeasureFilterRow> rows) {
    this.rows = rows;
    return this;
  }

  MeasureFilterResult setError(@Nullable Error err) {
    this.error = err;
    return this;
  }

  public boolean isSuccess() {
    return error == null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (rows != null) {
      sb.append(rows.size()).append(" rows, ");
    }
    if (error != null) {
      sb.append("error=").append(error).append(", ");
    }
    sb.append(durationInMs).append("ms");
    return sb.toString();
  }
}