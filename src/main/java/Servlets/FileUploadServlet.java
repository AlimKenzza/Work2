package Servlets;

import Classes.Dao;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Servlets.FileUploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)
public class FileUploadServlet extends HttpServlet {
    public static final String UPLOAD_DIR = "images";
    public String dbFileName = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Dao dao = new Dao();
        PrintWriter out = response.getWriter();

        int id = (Integer) session.getAttribute("id");
        String description = request.getParameter("description");

        Part part = request.getPart("file");//
        String fileName = extractFileName(part);//file name

        String applicationPath = getServletContext().getRealPath("");
        String uploadPath = applicationPath + File.separator + UPLOAD_DIR;
        System.out.println("applicationPath:" + applicationPath);
        File fileUploadDirectory = new File(uploadPath);
        if (!fileUploadDirectory.exists()) {
            fileUploadDirectory.mkdirs();
        }

        String savePath = uploadPath + File.separator + fileName;
        System.out.println("savePath: " + savePath);
        String sRootPath = new File(savePath).getAbsolutePath();
        System.out.println("sRootPath: " + sRootPath);
        part.write(savePath + File.separator);

        File fileSaveDir1 = new File(savePath);
        dbFileName = UPLOAD_DIR + File.separator + fileName;
        part.write(savePath + File.separator);

        if (dao.fileUpload(id, dbFileName, savePath, description)) {
            out.println("<center><h1>Image inserted Succesfully......</h1></center>");
            out.println("<center><a href='display.jsp?id=" + id + "'>Display</a></center>");
        } else {
            out.println("<center> Error occurred </center>");
        }
    }

    private String extractFileName(Part part) {//This method will print the file name.
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}