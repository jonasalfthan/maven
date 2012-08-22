package junit.runner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class defines the current version of JUnit
 */
public class Version {
    private static final String VERSION;
    static {
        final Properties properties= new Properties();
        try {
            final ClassLoader classLoader = Version.class.getClassLoader();
            InputStream pomProps= getPomPropertiesAsStream(classLoader, "junit");
            if (pomProps == null) pomProps = getPomPropertiesAsStream(classLoader, "junit-dep");
            if (pomProps != null) properties.load(pomProps);
            VERSION= properties.getProperty("version", "<version>");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static InputStream getPomPropertiesAsStream(ClassLoader cl, String artifactId) {
        return cl.getResourceAsStream("META-INF/maven/junit/" + artifactId + "/pom.properties");
    }

	private Version() {
		// don't instantiate
	}

	public static String id() {
		return VERSION;
	}
	
	public static void main(String[] args) {
		System.out.println(id());
	}
}
