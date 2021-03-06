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
package org.sonar.api.profiles;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.ServerComponent;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.ActiveRuleParam;
import org.sonar.api.utils.SonarException;

import java.io.IOException;
import java.io.Writer;

/**
 * @since 2.3
 */
public final class XMLProfileSerializer implements ServerComponent {

  public void write(RulesProfile profile, Writer writer) {
    try {
      appendHeader(profile, writer);
      appendRules(profile, writer);
      appendAlerts(profile, writer);
      appendFooter(writer);

    } catch (IOException e) {
      throw new SonarException("Fail to export the profile " + profile, e);
    }
  }

  private void appendHeader(RulesProfile profile, Writer writer) throws IOException {
    writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<!-- Generated by Sonar -->"
        + "<profile><name>");
    StringEscapeUtils.escapeXml(writer, profile.getName());
    writer.append("</name><language>");
    StringEscapeUtils.escapeXml(writer, profile.getLanguage());
    writer.append("</language>");
  }

  private void appendRules(RulesProfile profile, Writer writer) throws IOException {
    if (!profile.getActiveRules().isEmpty()) {
      writer.append("<rules>");
      for (ActiveRule activeRule : profile.getActiveRules()) {
        appendRule(activeRule, writer);
      }
      writer.append("</rules>");
    }
  }

  private void appendRule(ActiveRule activeRule, Writer writer) throws IOException {
    writer.append("<rule><repositoryKey>");
    writer.append(activeRule.getRepositoryKey());
    writer.append("</repositoryKey><key>");
    StringEscapeUtils.escapeXml(writer, activeRule.getRuleKey());
    writer.append("</key>");
    if (activeRule.getSeverity() != null) {
      writer.append("<priority>");
      writer.append(activeRule.getSeverity().name());
      writer.append("</priority>");
    }
    appendRuleParameters(activeRule, writer);
    writer.append("</rule>");
  }

  private void appendRuleParameters(ActiveRule activeRule, Writer writer) throws IOException {
    if (activeRule.getActiveRuleParams() != null && !activeRule.getActiveRuleParams().isEmpty()) {
      writer.append("<parameters>");
      for (ActiveRuleParam activeRuleParam : activeRule.getActiveRuleParams()) {
        appendRuleParameter(writer, activeRuleParam);
      }
      writer.append("</parameters>");
    }
  }

  private void appendRuleParameter(Writer writer, ActiveRuleParam activeRuleParam) throws IOException {
    if (StringUtils.isNotBlank(activeRuleParam.getValue())) {
      writer.append("<parameter><key>");
      StringEscapeUtils.escapeXml(writer, activeRuleParam.getKey());
      writer.append("</key><value>");
      StringEscapeUtils.escapeXml(writer, activeRuleParam.getValue());
      writer.append("</value>");
      writer.append("</parameter>");
    }
  }

  private void appendAlerts(RulesProfile profile, Writer writer) throws IOException {
    if (!profile.getAlerts().isEmpty()) {
      writer.append("<alerts>");
      for (Alert alert : profile.getAlerts()) {
        appendAlert(alert, writer);
      }
      writer.append("</alerts>");
    }
  }

  private void appendAlert(Alert alert, Writer writer) throws IOException {
    writer.append("<alert><metric>");
    StringEscapeUtils.escapeXml(writer, alert.getMetric().getKey());
    writer.append("</metric>");
    if (alert.getPeriod() !=null) {
      writer.append("<period>");
      StringEscapeUtils.escapeXml(writer, Integer.toString(alert.getPeriod()));
      writer.append("</period>");
    }
    writer.append("<operator>");
    StringEscapeUtils.escapeXml(writer, alert.getOperator());
    writer.append("</operator>");
    writer.append("<warning>");
    StringEscapeUtils.escapeXml(writer, alert.getValueWarning());
    writer.append("</warning>");
    writer.append("<error>");
    StringEscapeUtils.escapeXml(writer, alert.getValueError());
    writer.append("</error></alert>");
  }

  private void appendFooter(Writer writer) throws IOException {
    writer.append("</profile>");
  }
}
