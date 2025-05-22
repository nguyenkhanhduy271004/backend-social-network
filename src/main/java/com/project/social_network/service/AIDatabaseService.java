package com.project.social_network.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AIDatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(AIDatabaseService.class);
    private final OllamaChatModel chatModel;
    private final JdbcTemplate jdbcTemplate;

    // Pattern to detect harmful SQL operations while allowing SELECT
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "(?i)(DROP|DELETE|UPDATE|INSERT|ALTER|TRUNCATE|EXEC|UNION|--|;|/\\*|\\*/|xp_|sp_)");

    // Pattern to ensure query starts with SELECT
    private static final Pattern SELECT_PATTERN = Pattern.compile(
            "^\\s*SELECT\\s+", Pattern.CASE_INSENSITIVE);

    @Autowired
    public AIDatabaseService(OllamaChatModel chatModel, JdbcTemplate jdbcTemplate) {
        this.chatModel = chatModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> processNaturalLanguageQuery(String query) {
        // Validate input
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be empty");
        }

        logger.info("Processing natural language query: {}", query);

        // Create system prompt with database schema information
        String systemPrompt = """
                You are a SQL query generator. Convert natural language questions into simple SQL SELECT queries.

                Database Schema:
                - User (id, fullName, email, password, mobile, image, bio, createdDate)
                - Post (id, content, image, user_id, createdDate)
                - Comment (id, content, user_id, post_id, createdDate)
                - Like (id, user_id, post_id, createdDate)
                - Group (id, name, description, createdDate)
                - Message (id, content, sender_id, receiver_id, createdDate)

                Rules:
                1. ALWAYS start with SELECT
                2. Use simple queries when possible
                3. Only add JOINs when necessary
                4. Use proper table names (Post, User, etc.)

                Example Queries:
                Natural: "Show all posts"
                SQL: SELECT * FROM Post

                Natural: "Show all users"
                SQL: SELECT * FROM User

                Natural: "Show posts with user information"
                SQL: SELECT p.*, u.fullName FROM Post p JOIN User u ON p.user_id = u.id

                Your response should ONLY contain the SQL query, nothing else.
                """;

        // Generate SQL query using AI
        Message systemMessage = new SystemMessage(systemPrompt);
        Message userMessage = new UserMessage("Convert to SQL: " + query);
        String aiResponse = chatModel.call(new Prompt(List.of(systemMessage, userMessage)))
                .getResult().getOutput().getContent().trim();

        // Extract only the SQL query from the response
        String sqlQuery = aiResponse;
        if (aiResponse.contains("SELECT")) {
            // Find the last SELECT statement in case there are multiple
            int lastSelectIndex = aiResponse.lastIndexOf("SELECT");
            sqlQuery = aiResponse.substring(lastSelectIndex);
            // Remove any text after the SQL query
            int endIndex = sqlQuery.indexOf("\n");
            if (endIndex > 0) {
                sqlQuery = sqlQuery.substring(0, endIndex).trim();
            }
        }

        logger.info("Generated SQL query: {}", sqlQuery);

        // Validate generated SQL
        if (!SELECT_PATTERN.matcher(sqlQuery).find()) {
            logger.error("Invalid SQL query - must start with SELECT. Generated query: {}", sqlQuery);
            throw new SecurityException("Query must start with SELECT. Generated query: " + sqlQuery);
        }

        if (SQL_INJECTION_PATTERN.matcher(sqlQuery).find()) {
            logger.error("Potentially harmful SQL detected in query: {}", sqlQuery);
            throw new SecurityException("Query contains potentially harmful SQL operations");
        }

        // Execute query
        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sqlQuery);
            logger.info("Query executed successfully. Result count: {}", results.size());
            return results;
        } catch (Exception e) {
            logger.error("Error executing query: {}", sqlQuery, e);
            throw new RuntimeException("Error executing query: " + e.getMessage());
        }
    }
}