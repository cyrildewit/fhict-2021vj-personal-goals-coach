package com.cyrildewit.pgc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.Subgoal;
import com.cyrildewit.pgc.dao.SubgoalDao;
import com.cyrildewit.pgc.dao.InMemorySubgoalDao;

@Service
public class SubgoalService
{
    private final SubgoalDao subgoalDao;

    @Autowired
    public SubgoalService(InMemorySubgoalDao subgoalDao)
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

    public Long getTotalSubgoalsCountForGoal(Goal goal)
    {
        return subgoalDao.countAllSubgoalsForGoal(goal);
    }

    public Optional<Subgoal> findSubgoalById(Long id)
    {
        return subgoalDao.findSubgoalById(id);
    }

    public Optional<Subgoal> findSubgoalByUuid(UUID uuid)
    {
        return subgoalDao.findSubgoalByUuid(uuid);
    }

    public void addSubgoal(Subgoal subgoal)
    {
        subgoalDao.insertSubgoal(subgoal);
    }

    public void updateSubgoal(Subgoal subgoal, String[] params)
    {
        subgoalDao.insertSubgoal(subgoal);
    }

    public void deleteSubgoalById(Long id)
    {
        subgoalDao.deleteSubgoalById(id);
    }

    public void deleteSubgoal(Subgoal subgoal)
    {
        subgoalDao.deleteSubgoal(subgoal);
    }
}
