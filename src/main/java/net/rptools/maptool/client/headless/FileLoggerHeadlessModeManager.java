package net.rptools.maptool.client.headless;

import net.rptools.maptool.language.I18N;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JOptionPane; // Only for JOptionPane constants

/**
 * Implementation of {@link HeadlessModeManager} for headless operation.
 * Messages are logged to a file, and UI interactions are suppressed or
 * handled with default actions.
 */
public class FileLoggerHeadlessModeManager implements HeadlessModeManager {
    private static final Logger log = LogManager.getLogger(FileLoggerHeadlessModeManager.class);
    // This specific logger can be configured differently in log4j2.xml if needed,
    // for example, to only log messages from headless mode operations.
    private static final Logger headlessLog = LogManager.getLogger("HeadlessUI");


    @Override
    public boolean isHeadless() {
        return true;
    }

    private String formatMessage(String messageKey, Throwable throwable, Object... params) {
        String msgPart;
        if (params != null && params.length > 0) {
            msgPart = I18N.getText(messageKey, params);
        } else {
            msgPart = I18N.getText(messageKey);
        }
        if (throwable != null) {
            return msgPart + " | Exception: " + throwable.toString();
        }
        return msgPart;
    }

    @Override
    public void showError(String messageKey, Throwable throwable, Object... params) {
        String message = formatMessage(messageKey, throwable, params);
        headlessLog.error("HEADLESS_ERROR: {} (Key: {})", message, messageKey, throwable);
    }

    @Override
    public void showWarning(String messageKey, Throwable throwable, Object... params) {
        String message = formatMessage(messageKey, throwable, params);
        headlessLog.warn("HEADLESS_WARNING: {} (Key: {})", message, messageKey, throwable);
    }

    @Override
    public void showInformation(String messageKey, Throwable throwable, Object... params) {
        String message = formatMessage(messageKey, throwable, params);
        headlessLog.info("HEADLESS_INFO: {} (Key: {})", message, messageKey, throwable);
    }

    @Override
    public boolean confirm(String messageKey, Object... params) {
        String message = formatMessage(messageKey, null, params);
        headlessLog.info("HEADLESS_CONFIRM_REQUESTED: {} (Key: {}). Auto-confirming TRUE.", message, messageKey);
        // Default behavior in headless mode: auto-confirm.
        // This might need to be configurable or different for specific confirmations.
        return true;
    }

    @Override
    public int confirmImpl(String titleKey, int buttons, String messageKey, Object... params) {
        String title = I18N.getText(titleKey);
        String message = formatMessage(messageKey, null, params); // formatMessage handles I18N.getText with params

        String buttonTypeStr;
        int defaultReturnOption = JOptionPane.YES_OPTION; // Default to YES for most cases

        switch (buttons) {
            case JOptionPane.YES_NO_OPTION:
                buttonTypeStr = "YES_NO_OPTION";
                defaultReturnOption = JOptionPane.YES_OPTION;
                break;
            case JOptionPane.YES_NO_CANCEL_OPTION:
                buttonTypeStr = "YES_NO_CANCEL_OPTION";
                defaultReturnOption = JOptionPane.YES_OPTION;
                break;
            case JOptionPane.OK_CANCEL_OPTION:
                buttonTypeStr = "OK_CANCEL_OPTION";
                defaultReturnOption = JOptionPane.OK_OPTION;
                break;
            default:
                buttonTypeStr = "UNKNOWN_OPTION_TYPE (" + buttons + ")";
                defaultReturnOption = JOptionPane.YES_OPTION; // Fallback
                break;
        }

        headlessLog.info("HEADLESS_CONFIRM_IMPL_REQUESTED: Title: '{}' (Key: {}), Message: '{}' (Key: {}), Buttons: {}. Auto-replying with default option: {}.",
                title, titleKey, message, messageKey, buttonTypeStr, defaultReturnOption);

        // For critical decisions like "save campaign on close", returning YES/OK might be problematic
        // if the headless server isn't equipped to handle the subsequent action (e.g., show a file dialog).
        // This default might need refinement based on specific use cases encountered.
        // For example, for "confirm token delete", YES_OPTION is fine.
        // For "save campaign before closing if dirty", YES_OPTION might be problematic if it then tries to show a save dialog.
        // This will be reviewed during testing of specific dialogs.
        return defaultReturnOption;
    }

    @Override
    public void handleDialog(String dialogType, String... details) {
        String detailString = details.length > 0 ? String.join(", ", details) : "No details";
        headlessLog.warn("HEADLESS_DIALOG_SUPPRESSED: Type: '{}', Details: [{}]. Operation that would show this dialog will likely not function as expected in GUI mode.",
                dialogType, detailString);
        // Optionally, could throw an UnsupportedOperationException if a dialog is critical and cannot be handled.
        // For example: if ("FileChooser".equals(dialogType)) {
        //    throw new UnsupportedOperationException("File chooser dialogs are not supported in headless mode.");
        // }
    }
}
