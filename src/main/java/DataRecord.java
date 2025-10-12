import java.time.LocalDateTime;

public record DataRecord(
    String source,
    String title,
    String content,
    LocalDateTime fetchedAt
) {}
