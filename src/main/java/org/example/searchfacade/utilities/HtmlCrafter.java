package org.example.searchfacade.utilities;

public class HtmlCrafter {

    private static final String DOC_TEMPLATE = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta http-equiv="X-UA-Compatible" content="ie=edge">
            <link rel="icon" type="image/x-icon" href="/img/koala-icon.ico">
            <link rel="stylesheet" href="/css/search-facade.css">
            <title>TLPT REST API</title>
        </head>
        <body>
            <main>
                <div class="%s">
<pre>
%s
</pre>
                </div>
            </main>
        </body>
        </html>
    """;

    private static final String CSS_CONTAINER_CLASS = "api-container";

    public static String insertToTemplate(String resource) {
        return String.format(DOC_TEMPLATE, CSS_CONTAINER_CLASS, resource);
    }

}
