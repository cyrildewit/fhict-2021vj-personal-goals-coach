package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.goal.dao.SubgoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SqlSubgoalDao;

@Service
public class SubgoalService implements SubgoalServiceInterface
{
    private final SubgoalDao subgoalDao;

    @Autowired
    public SubgoalService(SqlSubgoalDao subgoalDao)
    {
        this.subgoalDao = subgoalDao;
    }

    public List<Subgoal> getAllSubgoals()
    {
        return subgoalDao.selectAllSubgoals();
    }

    public List<Subgoal> getAllSubgoalsForGoal(Goal goal)
    {
        return subgoalDao.selectAllSubgoalsForGoal(goal);
    }

    public List<Subgoal> getAllFirstLevelSubgoals(Goal goal) {
        return subgoalDao.selectAllFirstLevelSubgoalsForGoal(goal);
    }

    public List<Subgoal> getAllSubgoalsForSubgoal(Subgoal subgoal)
    {
        return subgoalDao.selectAllSubgoalsForSubgoal(subgoal);
    }

    public long getTotalSubgoalsCountForGoal(Goal goal)
    {
        return subgoalDao.countAllSubgoalsForGoal(goal);
    }

    public long getTotalFirstLevelSubgoalsCountForGoal(Goal goal)
    {
        return subgoalDao.countAllFistLevelSubgoalsForGoal(goal);
    }

    public long getTotalSubgoalsCountForSubgoal(Subgoal subgoal)
    {
        return subgoalDao.countAllSubgoalsForSubgoal(subgoal);
    }

    public long getTotalFirstLevelSubgoalsCountForSubgoal(Subgoal subgoal)
    {
        return subgoalDao.countAllFistLevelSubgoalsForSubgoal(subgoal);
    }

    public Optional<Subgoal> getSubgoalById(long id)
    {
        return subgoalDao.findSubgoalById(id);
    }

    public Optional<Subgoal> getSubgoalByUuid(UUID uuid)
    {
        return subgoalDao.findSubgoalByUuid(uuid);
    }

    public void addSubgoal(Subgoal subgoal)
    {
        subgoalDao.insertSubgoal(subgoal);
    }

    public void updateSubgoal(Subgoal subgoal)
    {
        subgoalDao.updateSubgoal(subgoal);
    }

    public void deleteSubgoalById(long id)
    {
        subgoalDao.deleteSubgoalById(id);
    }

    public void deleteSubgoal(Subgoal subgoal)
    {
        subgoalDao.deleteSubgoal(subgoal);
    }

    public boolean determineIfSubgoalBelongsToGoal(Subgoal subgoal, Goal goal)
    {
        return subgoal.getGoalId() == goal.getId();
    }

    /**
     * Level must be >= 1.
     *
     * 0 is reserverd for goal.
     */
    public Integer determineSubgoalLevel(Subgoal subgoal)
    {
        return subgoalDao.determineSubgoalLevel(subgoal);
    }
}
