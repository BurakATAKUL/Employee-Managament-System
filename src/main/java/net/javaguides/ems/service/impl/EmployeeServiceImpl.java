package net.javaguides.ems.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.exception.ResoruceNotFoundException;
import net.javaguides.ems.mapper.EmployeeMapper;
import net.javaguides.ems.repository.EmployeeRepository;
import net.javaguides.ems.service.EmployeeService;
import net.javaguides.ems.service.QRCodeGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private QRCodeGenerateService qrCodeGenerateService;


    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
       Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new RuntimeException("Employee is not exists with given id : " + employeeId));

        return EmployeeMapper.mapToEmployeeDto(employee);
    }



    @Override
    public List<EmployeeDto> getAllEmployees() {

       List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResoruceNotFoundException("Employee is not exists with given id : " + employeeId)
        );

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        Employee updatedEmployeeObj = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(//verilen id ye ait bir id db de yok ise hata fırlat
                () -> new ResoruceNotFoundException("Employee is not exists with given id : " + employeeId)
        );
        employeeRepository.deleteById(employeeId);

    }

    @Autowired
    QRCodeGenerateServiceImpl qrCodeGenerateServiceImpl;

    @Override
    public void generateAndSaveQrCodeForEmployee(Long employeeId) throws Exception {
        Employee employee = employeeRepository.findById(employeeId) //eğer employe id ye ait eployee yoksa hata fırlat
                .orElseThrow(() -> new RuntimeException("Employee is not exists with given id : " + employeeId));

        String qrCodeText = String.format("ID: %d, FirstName: %s, LastName: %s, Email: %s"
        , employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail());

        String qrCodeBase64 = qrCodeGenerateServiceImpl.generateQRCode(qrCodeText, 50, 50);
        employee.setQr_Code(qrCodeBase64);
        employeeRepository.save(employee);
    }

    @Override
    public String getQrCodeForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee is not exists with given id : " + employeeId));
        return employee.getQr_Code();
    }


}
