package me.ningsk.common.stream;

import java.io.InputStream;

public class ClosedInputStream extends InputStream
{
    public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

    public int read()
    {
        return -1;
    }
}
