package log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Classe utilitaire pour generer les loggers Log4j.
 * Calquee sur LoggerUtility du prof — Tree V3.
 * Genere les logs dans un fichier texte ou HTML.
 */
public class LoggerUtility {

	private static final String TEXT_LOG_CONFIG = "src/log/log4j-text.properties";
	private static final String HTML_LOG_CONFIG = "src/log/log4j-html.properties";

	/**
	 * Retourne un logger configure selon le type de fichier souhaite.
	 * Type "text" → fichier .log lisible.
	 * Type "html" → fichier .html visualisable dans un navigateur.
	 *
	 * @param logClass    la classe qui genere les logs
	 * @param logFileType "text" ou "html"
	 * @return le logger configure
	 */
	public static Logger getLogger(Class<?> logClass, String logFileType) {
		if (logFileType.equals("text")) {
			PropertyConfigurator.configure(TEXT_LOG_CONFIG);
		} else if (logFileType.equals("html")) {
			PropertyConfigurator.configure(HTML_LOG_CONFIG);
		} else {
			throw new IllegalArgumentException("Type de log inconnu : " + logFileType);
		}
		return Logger.getLogger(logClass.getName());
	}
}