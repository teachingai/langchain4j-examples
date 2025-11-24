package com.github.teachingai.ollama.tools;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.agent.tool.JsonSchemaProperty.*;

public class FunctionConfig {

    public static List<ToolSpecification> createToolSpecifications() {
        List<ToolSpecification> toolSpecifications = new ArrayList<>();

        toolSpecifications.add(ToolSpecification.builder()
                .name("getWeather")
                .description("Returns the weather forecast for a given city")
                .addParameter("city", type("string"), description("The city for which the weather forecast should be returned"))
                .addParameter("unit", enums(GetWeatherFunction.TemperatureUnit.class)) // enum TemperatureUnit { CELSIUS, FAHRENHEIT }
                .build());

        toolSpecifications.addAll(ToolSpecifications.toolSpecificationsFrom(new GetWeatherFunction()));
        toolSpecifications.addAll(ToolSpecifications.toolSpecificationsFrom(new PconlineRegionFunction()));

        return toolSpecifications;
    }

}
