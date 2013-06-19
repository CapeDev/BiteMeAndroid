package com.thoughtworks.trakemoi.support;

import com.thoughtworks.trakemoi.gateways.search.wiretypes.SearchWireType;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class SearchJson {
    private SearchWireType searchWireType;

    public static SearchJson searchJson() {
        return new SearchJson();
    }

    public SearchJson from(SearchWireType searchWireType) {
        this.searchWireType = searchWireType;
        return this;
    }

    public String asString() {
        StringWriter writer = new StringWriter();

        Template template = getTemplate();
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("root", searchWireType);

        try {
            template.process(input, writer);
        } catch (TemplateException exception) {
            throw new RuntimeException(exception);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return writer.toString();
    }

    private static Template getTemplate() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(SearchWireTypeBuilder.class, "/templates/");
        try {
            return configuration.getTemplate("search_json.ftl");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
