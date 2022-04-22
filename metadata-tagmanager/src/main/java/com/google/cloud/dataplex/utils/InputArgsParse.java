package com.google.cloud.dataplex.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputArgsParse {
    private static final Logger LOGGER = LoggerFactory.getLogger(InputArgsParse.class);


    private static final String TAG_TEMPLATE_ID_OPT = "tag_template_id";
    private static final String PROJECT_NAME_OPT = "project_id";
    private static final String LOCATION_OPT = "location";
    private static final String LAKE_ID_OPT = "lake_id";
    private static final String ZONE_ID_OPT = "zone_id";
    private static final String ENTITY_ID_OPT = "entity_id";
    private static final String INPUT_FILE_OPT = "input_file";

    private static final Option TAG_TEMPLATE_ID_OPTION = Option.builder("TEMPLATE_ID").longOpt(TAG_TEMPLATE_ID_OPT)
            .desc("the name of the tag template in Data catalog to use for tagging the data product")
            .hasArg()
            .build();

    private static final Option PROJECT_OPTION = Option.builder("PROJECT_NAME").longOpt(PROJECT_NAME_OPT)
            .desc("project id of the dataplex entity")
            .hasArg()
            .build();

    private static final Option LOCATION_OPTION = Option.builder("LOCATION").longOpt(LOCATION_OPT)
            .desc("location of the dataplex entity")
            .hasArg()
            .build();

    private static final Option LAKE_ID_OPTION = Option.builder("LAKE_ID").longOpt(LAKE_ID_OPT)
            .desc("lake id of the dataplex entity")
            .hasArg()
            .build();
    private static final Option ZONE_ID_OPTION = Option.builder("ZONE_ID").longOpt(ZONE_ID_OPT)
            .desc("zone if of the dataplex entity")
            .hasArg()
            .build();

    private static final Option ENTITY_ID_OPTION = Option.builder("ENTITY_ID").longOpt(ENTITY_ID_OPT)
            .desc("entity id of the data product")
            .hasArg()
            .build();

    private static final Option INPUT_FILE_OPTION = Option.builder("INPUT_FILE").longOpt(INPUT_FILE_OPT)
            .desc("Input yaml file tag of the data product")
            .hasArg()
            .build();

    private static final Options options = new Options().addOption(TAG_TEMPLATE_ID_OPTION).addOption(PROJECT_OPTION)
            .addOption(LOCATION_OPTION).addOption(LAKE_ID_OPTION).addOption(ZONE_ID_OPTION)
            .addOption(ENTITY_ID_OPTION).addOption(INPUT_FILE_OPTION);

    /**
     * Parse command line arguments
     *
     * @param args command line arguments
     * @return parsed arguments
     */
    public static CommandLine parseArguments(String... args) {
        CommandLineParser parser = new DefaultParser();
        LOGGER.info("Parsing arguments {}", (Object) args);
        try {
            parser.parse(options, args, true);
            return parser.parse(options, args, true);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }



}
