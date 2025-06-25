package net.rptools.maptool.client.headless;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JOptionPane;

/**
 * Implementation of {@link HeadlessModeManager} for standard Swing-based UI.
 * This replicates the existing dialog behavior of MapTool.
 */
public class SwingHeadlessModeManager implements HeadlessModeManager {
    private static final Logger log = LogManager.getLogger(SwingHeadlessModeManager.class);

    @Override
    public boolean isHeadless() {
        return false;
    }

    private String generateMessage(String messageKey, Throwable throwable, Object... params) {
        String msg = "";
        if (messageKey != null) {
            if (params != null && params.length > 0) {
                msg = I18N.getText(messageKey, params);
            } else {
                msg = I18N.getText(messageKey);
            }
        }
        if (throwable != null) {
            if (messageKey != null) {
                msg = msg + "<br/>" + throwable.toString();
            } else {
                msg = throwable.toString();
            }
        }
        return msg.replace("\n", "<br/>");
    }

    @Override
    public void showError(String messageKey, Throwable throwable, Object... params) {
        String message = generateMessage(messageKey, throwable, params);
        // Log with params if any, matching original MapTool behavior
        if (params != null && params.length > 0) {
            log.error(I18N.getText(messageKey, params), throwable);
        } else {
            log.error(I18N.getText(messageKey), throwable);
        }
        JOptionPane.showMessageDialog(
                MapTool.getFrame(),
                "<html>" + message,
                I18N.getText("msg.title.messageDialogError"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void showWarning(String messageKey, Throwable throwable, Object... params) {
        String message = generateMessage(messageKey, throwable, params);
        if (params != null && params.length > 0) {
            log.warn(I18N.getText(messageKey, params), throwable);
        } else {
            log.warn(I18N.getText(messageKey), throwable);
        }
        JOptionPane.showMessageDialog(
                MapTool.getFrame(),
                "<html>" + message,
                I18N.getText("msg.title.messageDialogWarning"),
                JOptionPane.WARNING_MESSAGE
        );
    }

    @Override
    public void showInformation(String messageKey, Throwable throwable, Object... params) {
        String message = generateMessage(messageKey, throwable, params);
        if (params != null && params.length > 0) {
            log.info(I18N.getText(messageKey, params), throwable);
        } else {
            log.info(I18N.getText(messageKey), throwable);
        }
        JOptionPane.showMessageDialog(
                MapTool.getFrame(),
                "<html>" + message,
                I18N.getText("msg.title.messageDialogInfo"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public boolean confirm(String messageKey, Object... params) {
        String title = I18N.getText("msg.title.messageDialogConfirm");
        String message = I18N.getText(messageKey, params);
        log.debug(messageKey); // Original logged the key
        return JOptionPane.showConfirmDialog(
                MapTool.getFrame(),
                message,
                title,
                JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION;
    }

    @Override
    public int confirmImpl(String titleKey, int buttons, String messageKey, Object... params) {
        String title = I18N.getText(titleKey);
        String message = I18N.getText(messageKey, params);
        log.debug(messageKey); // Original logged the key
        return JOptionPane.showConfirmDialog(
                MapTool.getFrame(),
                message,
                title,
                buttons
        );
    }

    @Override
    public void handleDialog(String dialogType, String... details) {
        // In Swing mode, actual dialogs are created elsewhere.
        // This method could be used if we later want to centralize even more dialog creation.
        // For now, logging a warning if it's unexpectedly called might be useful.
        String detailString = details.length > 0 ? String.join(", ", details) : "No details";
        log.debug("Generic 'handleDialog' called in Swing mode for type: {} with details: {}. This is unexpected.", dialogType, detailString);
        // Or, if certain dialogs are expected to be routed here:
        // if ("SpecificDialogHandledBySwingManager".equals(dialogType)) { /* show actual dialog */ }
    }
}
