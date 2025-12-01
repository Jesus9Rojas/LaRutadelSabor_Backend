package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IDocumentoDAO;
import com.RutaDelSabor.ruta.models.entities.Documento;


@Service
public class DocumentoServiceImpl implements IDocumentoService {

    
    @Autowired
    private IDocumentoDAO documentoDAO;

    @Override
	public List<Documento> GetAll() {
		List<Documento> list = new ArrayList<Documento>(); 
		documentoDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public Documento Save(Documento object) {
		return documentoDAO.save(object);
	}

	@Override
	public Documento FindByID(Long id) {
		return documentoDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(Documento object) {
		documentoDAO.delete(object);
	}
    

}
