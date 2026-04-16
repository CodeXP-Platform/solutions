package com.codexp.solutions.solutions.domain.services;

import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.commands.CreateSolutionFromRequestedEventCommand;
import com.codexp.solutions.solutions.domain.model.commands.SubmitSolutionCommand;
import com.codexp.solutions.solutions.domain.model.commands.UpdateSolutionCodeCommand;

public interface SolutionCommandService {
    Solution handle(CreateSolutionFromRequestedEventCommand command);

    Solution handle(UpdateSolutionCodeCommand command);

    Solution handle(SubmitSolutionCommand command);
}
