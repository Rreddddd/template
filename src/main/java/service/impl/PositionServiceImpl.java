package service.impl;

import dao.PositionDao;
import org.springframework.stereotype.Service;
import service.PositionService;

import javax.annotation.Resource;

@Service
public class PositionServiceImpl implements PositionService {

    @Resource
    private PositionDao positionDao;
}
