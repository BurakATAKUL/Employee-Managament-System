package net.javaguides.ems.controller;

import lombok.AllArgsConstructor;
import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    //build add employee rest api
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    //Build get employee rest api
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId) {
      EmployeeDto employeeDto =  employeeService.getEmployeeById(employeeId);
      return ResponseEntity.ok(employeeDto);
    }

    //Build Get All Employees REST API
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);

    }

    // Build Update Employee REST Api
    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long id,
                                                      @RequestBody EmployeeDto updateEmployee) { //yeniden döndürmesi için resquestbody kullandık

        EmployeeDto employeeDto = employeeService.updateEmployee(id, updateEmployee);
        return ResponseEntity.ok(employeeDto);

    }


    //Build Delete Employee REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
         employeeService.deleteEmployee(employeeId);
         return ResponseEntity.ok("Employee deleted successfully : " + employeeId);
    }

    @PostMapping("/generate-employee-qr-code/{employeeId}")
    public String generateEmployeeQRCode(@PathVariable  Long employeeId) {
        try {
            employeeService.generateAndSaveQrCodeForEmployee(employeeId);
            return "QR Code generated successfully : " + employeeId;
        } catch (Exception e) {
            return "Error generatting QR Code: " + e.getMessage();
        }
    }


    @GetMapping("/qr-code/{employeeId}")
    public ResponseEntity<String> getEmployeeQrCode(@PathVariable  Long employeeId) {
        try {
            String qrCodeBase64 = employeeService.getQrCodeForEmployee(employeeId);
            return ResponseEntity.ok(qrCodeBase64);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching QR Code: " + e.getMessage());
        }
    }


}
