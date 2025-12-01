package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IEmpleadoDAO;
import com.RutaDelSabor.ruta.models.entities.Empleado;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService {

    @Autowired
     private IEmpleadoDAO empleadoDAO;

    @Override
	public List<Empleado> GetAll() {
		List<Empleado> list = new ArrayList<Empleado>(); 
		empleadoDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public Empleado Save(Empleado object) {
		return empleadoDAO.save(object);
	}

	@Override
	public Empleado FindByID(Long id) {
		return empleadoDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(Empleado object) {
		empleadoDAO.delete(object);
	}

}
