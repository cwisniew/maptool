package net.rptools.maptool.client.functions;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.client.MapToolVariableResolver;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.VariableResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
// No Mockito static imports needed for this basic test structure yet.

@ExtendWith(MockitoExtension.class)
public class HeadlessMacroFunctionsTest {

    private Parser parser;
    private VariableResolver resolver;
    private boolean originalHeadlessState;
    private String originalHeadlessProperty;
    private Field isHeadlessModeField;


    @Mock
    private Token token; // Mocked token for the resolver

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Save original headless state & set to true for tests
        isHeadlessModeField = MapTool.class.getDeclaredField("isHeadlessMode");
        isHeadlessModeField.setAccessible(true);
        originalHeadlessState = isHeadlessModeField.getBoolean(null);
        isHeadlessModeField.setBoolean(null, true); // Enable headless mode

        originalHeadlessProperty = System.getProperty("java.awt.headless");
        System.setProperty("java.awt.headless", "true");

        parser = new Parser(); // A new parser for each test
        resolver = new MapToolVariableResolver(token);
        // In a real scenario, you might need to register functions with the parser.
        // For now, we will call childEvaluate directly.
    }

    @AfterEach
    void tearDown() throws IllegalAccessException {
        // Restore original headless state
        if (isHeadlessModeField != null) {
            isHeadlessModeField.setBoolean(null, originalHeadlessState);
        }

        if (originalHeadlessProperty != null) {
            System.setProperty("java.awt.headless", originalHeadlessProperty);
        } else {
            System.clearProperty("java.awt.headless");
        }
    }

    @Test
    void testInputFunctionInHeadlessMode() {
        InputFunction inputFunction = InputFunction.getInstance();

        Exception exception = assertThrows(ParserException.class, () -> {
            inputFunction.childEvaluate(parser, resolver, "input", Collections.singletonList("var|val|prompt"));
        });

        assertEquals("input function is not available in headless mode.", exception.getMessage());
    }

    // MacroDialogFunctions Tests

    // UI Creation Functions
    @Test
    void testHtmlFrameInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = new ArrayList<>();
        params.add("testFrame"); // name
        params.add("http://localhost"); // url
        params.add(""); // opts

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "html.frame", params);
        });
        assertEquals("html.frame function is not available in headless mode.", exception.getMessage());
    }

    @Test
    void testHtmlDialogInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = new ArrayList<>();
        params.add("testDialog"); // name
        params.add("http://localhost"); // url
        params.add(""); // opts

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "html.dialog", params);
        });
        assertEquals("html.dialog function is not available in headless mode.", exception.getMessage());
    }

    @Test
    void testHtmlOverlayInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = new ArrayList<>();
        params.add("testOverlay"); // name
        params.add("http://localhost"); // url
        params.add(""); // opts

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "html.overlay", params);
        });
        assertEquals("html.overlay function is not available in headless mode.", exception.getMessage());
    }

    // UI Query Functions
    @Test
    void testIsDialogVisibleInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testDialog"); // name

        Object result = mdf.childEvaluate(parser, resolver, "isDialogVisible", params);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testIsFrameVisibleInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testFrame"); // name

        Object result = mdf.childEvaluate(parser, resolver, "isFrameVisible", params);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testIsOverlayRegisteredInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testOverlay"); // name

        Object result = mdf.childEvaluate(parser, resolver, "isOverlayRegistered", params);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testIsOverlayVisibleInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testOverlay"); // name

        Object result = mdf.childEvaluate(parser, resolver, "isOverlayVisible", params);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testIsOverlayLockedInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testOverlay"); // name

        Object result = mdf.childEvaluate(parser, resolver, "isOverlayLocked", params);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testGetFramePropertiesInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testFrame"); // name

        Object result = mdf.childEvaluate(parser, resolver, "getFrameProperties", params);
        assertEquals("", result);
    }

    @Test
    void testGetDialogPropertiesInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testDialog"); // name

        Object result = mdf.childEvaluate(parser, resolver, "getDialogProperties", params);
        assertEquals("", result);
    }

    @Test
    void testGetOverlayPropertiesAllInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("*"); // name

        Object result = mdf.childEvaluate(parser, resolver, "getOverlayProperties", params);
        assertEquals("[]", result);
    }

    @Test
    void testGetOverlayPropertiesSpecificInHeadlessMode() throws ParserException {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("specificOverlay"); // name

        Object result = mdf.childEvaluate(parser, resolver, "getOverlayProperties", params);
        assertEquals("", result);
    }

    // UI Modification Functions
    @Test
    void testCloseDialogInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testDialog"); // name

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "closeDialog", params);
        });
        assertEquals("closeDialog function is not available in headless mode.", exception.getMessage());
    }

    @Test
    void testCloseFrameInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testFrame"); // name

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "closeFrame", params);
        });
        assertEquals("closeFrame function is not available in headless mode.", exception.getMessage());
    }

    @Test
    void testResetFrameInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testFrame"); // name

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "resetFrame", params);
        });
        assertEquals("resetFrame function is not available in headless mode.", exception.getMessage());
    }

    @Test
    void testCloseOverlayInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Collections.singletonList("testOverlay"); // name

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "closeOverlay", params);
        });
        assertEquals("closeOverlay function is not available in headless mode.", exception.getMessage());
    }

    @Test
    void testSetOverlayVisibleInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = Arrays.asList("testOverlay", BigDecimal.ONE); // name, visible

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "setOverlayVisible", params);
        });
        assertEquals("setOverlayVisible function is not available in headless mode.", exception.getMessage());
    }

    @Test
    void testRunJsFunctionInHeadlessMode() {
        MacroDialogFunctions mdf = MacroDialogFunctions.getInstance();
        List<Object> params = new ArrayList<>();
        params.add("testFrame"); // name
        params.add("frame"); // type
        params.add("myJsFunc"); // func
        params.add("this"); // thisArg
        // params.add("[]"); // argsArray (optional)

        Exception exception = assertThrows(ParserException.class, () -> {
            mdf.childEvaluate(parser, resolver, "runJsFunction", params);
        });
        assertEquals("runJsFunction function is not available in headless mode.", exception.getMessage());
    }
}
