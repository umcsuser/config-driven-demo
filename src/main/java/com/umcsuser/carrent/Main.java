package com.umcsuser.carrent;

import com.umcsuser.carrent.repositories.VehicleCategoryConfigRepository;
import com.umcsuser.carrent.repositories.impl.VehicleCategoryConfigJsonRepository;
import com.umcsuser.carrent.repositories.impl.VehicleRepoMocap;
import com.umcsuser.carrent.repositories.VehicleRepository;
import com.umcsuser.carrent.services.VehicleCategoryConfigService;
import com.umcsuser.carrent.services.VehicleService;
import com.umcsuser.carrent.services.VehicleValidator;

public class Main {

    public static void main(String[] args) {
        VehicleCategoryConfigRepository configRepository = new VehicleCategoryConfigJsonRepository();
        VehicleCategoryConfigService configService = new VehicleCategoryConfigService(configRepository);
        VehicleRepository vehicleRepository = new VehicleRepoMocap();
        VehicleValidator validator = new VehicleValidator(configService);
        VehicleService vehicleService = new VehicleService(validator,vehicleRepository);

        UI ui = new UI(configService, vehicleService);
        ui.start();
    }
}

