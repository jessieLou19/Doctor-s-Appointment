package com.example.doctorappointment;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProjectScopeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_scope);

        WebView webView = findViewById(R.id.webViewScope);
        
        try {
            // Load markdown content from assets
            String markdownContent = readFromAssets("project_scope.md");
            
            // Convert markdown to HTML (simple conversion)
            String htmlContent = convertMarkdownToHtml(markdownContent);
            
            // Load the HTML content
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromAssets(String filename) throws IOException {
        StringBuilder buffer = new StringBuilder();
        try (InputStream inputStream = getAssets().open(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        }
        return buffer.toString();
    }

    private String convertMarkdownToHtml(String markdown) {
        // Very simple markdown to HTML conversion
        // For a real app, consider using a proper Markdown library
        String html = "<html><head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; padding: 16px; line-height: 1.6; }" +
                "h1 { color: #4B89DC; }" +
                "h2 { color: #4B89DC; border-bottom: 1px solid #eee; padding-bottom: 8px; }" +
                "h3 { color: #333; }" +
                "h4 { color: #555; }" +
                "ul { padding-left: 20px; }" +
                "li { margin-bottom: 8px; }" +
                "strong { color: #4B89DC; }" +
                "</style>" +
                "</head><body>";
        
        // Convert headers
        markdown = markdown.replaceAll("# (.*)", "<h1>$1</h1>");
        markdown = markdown.replaceAll("## (.*)", "<h2>$1</h2>");
        markdown = markdown.replaceAll("### (.*)", "<h3>$1</h3>");
        markdown = markdown.replaceAll("#### (.*)", "<h4>$1</h4>");
        
        // Convert bold text
        markdown = markdown.replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>");
        
        // Convert lists
        markdown = markdown.replaceAll("- (.*)", "<li>$1</li>");
        markdown = markdown.replaceAll("(?m)^<li>", "<ul><li>");
        markdown = markdown.replaceAll("(?m)^</li>$", "</li></ul>");
        
        // Convert paragraphs
        markdown = markdown.replaceAll("(?m)^([^<].*[^>])$", "<p>$1</p>");
        
        html += markdown + "</body></html>";
        return html;
    }
}