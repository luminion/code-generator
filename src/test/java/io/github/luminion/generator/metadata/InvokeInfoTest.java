package io.github.luminion.generator.metadata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvokeInfoTest {

    @Test
    void stringInvokeFormatSupportsMethodReferenceShorthand() {
        InvokeInfo invokeInfo = new InvokeInfo(
                "com.example.common.Result",
                "Result<%s>",
                "Result::success"
        );

        assertEquals("Result<String>", invokeInfo.toTypeString("String"));
        assertEquals("Result.success(data)", invokeInfo.toInvokeString("data"));
    }

    @Test
    void explicitFormatStringStillWorks() {
        InvokeInfo invokeInfo = new InvokeInfo(
                "com.example.common.Result",
                "Result<%s>",
                "Result.wrap(%s)"
        );

        assertEquals("Result.wrap(data)", invokeInfo.toInvokeString("data"));
    }
}
