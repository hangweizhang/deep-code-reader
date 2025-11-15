package com.code.deepreader.rpc.api.semantic;

import com.code.deepreader.rpc.api.semantic.dto.SemanticSnippetDTO;
import java.util.List;

public interface SemanticSearchRpcService {

    List<SemanticSnippetDTO> search(String query, int limit);
}

