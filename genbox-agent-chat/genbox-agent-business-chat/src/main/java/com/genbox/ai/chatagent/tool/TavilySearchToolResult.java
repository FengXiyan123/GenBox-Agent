package com.genbox.ai.chatagent.tool;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.genbox.ai.chatagent.model.SearchReference;

/**
 * 工具类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TavilySearchToolResult {

    private String query;
    private String answer;
    private List<SearchReference> results;
}
