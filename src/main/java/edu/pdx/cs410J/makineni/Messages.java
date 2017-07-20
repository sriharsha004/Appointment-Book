package edu.pdx.cs410J.makineni;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String getMappingCount( int count )
    {
        return String.format( "Server contains %d key/value pairs", count );
    }

    public static String formatKeyValuePair( String owner, String appointment )
    {
        return String.format("  %s -> %s", owner, appointment);
    }

    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String mappedKeyValue( String owner, String appointments )
    {
        return String.format( "Mapped %s to %s", owner, appointments );
    }

    public static String allMappingsDeleted() {
        return "All mappings have been deleted";
    }
}
