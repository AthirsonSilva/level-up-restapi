package com.api.levelup.services;

import jakarta.servlet.http.HttpServletResponse;

/**
 * This interface defines the methods for exporting the users database's data to
 * different file formats.
 * 
 * @see com.api.levelup.services.impl.AdminServiceImpl
 * 
 * @author Athirson Silva
 */
public interface AdminService {

	/**
	 * Exports the users database's data to an Excel file.
	 * 
	 * @param response the response object that will be used to download the file to
	 *                 the user
	 */
	public void exportToExcel(HttpServletResponse response);

	/**
	 * Exports the users database's data to a CSV file.
	 * 
	 * @param response the response object that will be used to download the file to
	 *                 the user
	 */
	public void exportToCSV(HttpServletResponse response);

	/**
	 * Exports the users database's data to a PDF file.
	 * 
	 * @param response the response object that will be used to download the file to
	 *                 the user
	 */
	public void exportToPDF(HttpServletResponse response);
}
