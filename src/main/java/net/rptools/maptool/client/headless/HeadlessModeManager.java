package net.rptools.maptool.client.headless;

/**
 * Manages behavior differences when MapTool is running in headless mode
 * versus with a graphical (Swing/JavaFX) user interface.
 */
public interface HeadlessModeManager {

    /**
     * Checks if MapTool is currently running in headless mode.
     *
     * @return {@code true} if in headless mode, {@code false} otherwise.
     */
    boolean isHeadless();

    /**
     * Shows an error message. In GUI mode, this would typically be a dialog.
     * In headless mode, this would be logged.
     *
     * @param messageKey The error message key to display or log.
     * @param throwable  The associated throwable, or {@code null} if none.
     * @param params     Optional parameters for formatting the message from the messageKey.
     */
    void showError(String messageKey, Throwable throwable, Object... params);

    /**
     * Shows a warning message. In GUI mode, this would typically be a dialog.
     * In headless mode, this would be logged.
     *
     * @param messageKey The warning message key to display or log.
     * @param throwable  The associated throwable, or {@code null} if none.
     * @param params     Optional parameters for formatting the message from the messageKey.
     */
    void showWarning(String messageKey, Throwable throwable, Object... params);

    /**
     * Shows an informational message. In GUI mode, this would typically be a dialog.
     * In headless mode, this would be logged.
     *
     * @param messageKey The informational message key to display or log.
     * @param throwable  The associated throwable, or {@code null} if none.
     * @param params     Optional parameters for formatting the message from the messageKey.
     */
    void showInformation(String messageKey, Throwable throwable, Object... params);

    /**
     * Asks for user confirmation. In GUI mode, this shows a confirmation dialog.
     * In headless mode, this logs the request and returns a default value or throws
     * an exception.
     *
     * @param messageKey The message/question key for the confirmation.
     * @param params     Optional parameters for formatting the message from the messageKey.
     * @return {@code true} if confirmed, {@code false} otherwise.
     */
    boolean confirm(String messageKey, Object... params);

    /**
     * A more flexible confirmation dialog handler, mirroring {@code JOptionPane.showConfirmDialog}.
     * In GUI mode, this shows a confirmation dialog with specified buttons.
     * In headless mode, this logs the request and returns a default option.
     *
     * @param title   The title for the confirmation dialog.
     * @param buttons The type of buttons to display (e.g., {@code JOptionPane.YES_NO_OPTION}).
     * @param message The message/question for the confirmation.
     * @param params  Optional parameters for formatting the message.
     * @return An integer representing the user's choice (e.g., {@code JOptionPane.YES_OPTION}).
     */
    int confirmImpl(String title, int buttons, String message, Object... params);

    /**
     * A generic handler for other types of dialogs or frames that might need
     * to be suppressed or handled differently in headless mode.
     *
     * @param dialogType A string identifying the type of dialog (e.g., "FileChooser", "PreferencesDialog").
     * @param details    Optional details about the dialog context.
     */
    void handleDialog(String dialogType, String... details);
}
