package com.codexp.solutions.solutions.domain.services;

import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.commands.CreateSolutionCommand;
import com.codexp.solutions.solutions.domain.model.commands.SubmitSolutionCommand;
import com.codexp.solutions.solutions.domain.model.commands.SubmitSolutionResult;
import com.codexp.solutions.solutions.domain.model.commands.UpdateSolutionCodeCommand;

public interface SolutionCommandService {
    Solution handle(CreateSolutionCommand command);

    Solution handle(UpdateSolutionCodeCommand command);

    SubmitSolutionResult handle(SubmitSolutionCommand command);
}
