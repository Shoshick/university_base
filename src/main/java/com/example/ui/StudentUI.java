package com.example.ui;

import com.example.model.Student;
import com.example.model.Faculty;
import com.example.model.Group;
import com.example.service.StudentService;

import java.util.List;
import java.util.Scanner;

public class StudentUI {

    private final StudentService studentService;
    private final Scanner scanner;

    public StudentUI(StudentService studentService) {
        this.studentService = studentService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("1. Просмотр всех студентов");
            System.out.println("2. Получить студента по зачетной книжке");
            System.out.println("3. Добавить нового студента");
            System.out.println("4. Обновить студента");
            System.out.println("5. Удалить студента");
            System.out.println("6. Поиск студентов");
            System.out.println("7. Выход");

            System.out.print("Выберите действие: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Введите корректное число!");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewAllStudents();
                    break;
                case 2:
                    viewStudentByGradeBook();
                    break;
                case 3:
                    addStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    searchStudent();
                    break;
                case 7:
                    System.out.println("Выход...");
                    return;
                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }

    private void viewAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("Нет студентов в базе данных.");
            } else {
                students.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при просмотре всех студентов: " + e.getMessage());
        }
    }

    private void viewStudentByGradeBook() {
        System.out.print("Введите номер зачетной книжки: ");
        String gradeBook = scanner.nextLine();
        try {
            Student student = studentService.getStudentByGradeBook(gradeBook);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("Студент с таким номером зачетной книжки не найден.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при получении студента: " + e.getMessage());
        }
    }

    private void addStudent() {
        try {
            System.out.print("Введите номер зачетной книжки: ");
            String gradeBook = scanner.nextLine();
            System.out.print("Введите ФИО студента: ");
            String fullName = scanner.nextLine();
            System.out.print("Введите ID факультета: ");
            Long facultyId = getValidatedLong();
            System.out.print("Введите ID группы: ");
            Long groupId = getValidatedLong();

            
            Faculty faculty = new Faculty();
            faculty.setFacultyId(facultyId);

            Group group = new Group();
            group.setGroupId(groupId);

            
            Student student = new Student();
            student.setGradeBook(gradeBook);
            student.setFullName(fullName);
            student.setFaculty(faculty);
            student.setGroup(group);

            studentService.addStudent(student);
            System.out.println("Студент добавлен.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении студента: " + e.getMessage());
        }
    }

    private void updateStudent() {
        System.out.print("Введите номер зачетной книжки студента для обновления: ");
        String gradeBook = scanner.nextLine();
        try {
            Student student = studentService.getStudentByGradeBook(gradeBook);
            if (student != null) {
                System.out.print("Введите новое ФИО студента: ");
                String fullName = scanner.nextLine();
                student.setFullName(fullName);
                studentService.updateStudent(student);
                System.out.println("Студент обновлен.");
            } else {
                System.out.println("Студент с таким номером зачетной книжки не найден.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении студента: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.print("Введите номер зачетной книжки студента для удаления: ");
        String gradeBook = scanner.nextLine();
        try {
            studentService.deleteStudent(gradeBook);
            System.out.println("Студент удален.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении студента: " + e.getMessage());
        }
    }

    private void searchStudent() {
        System.out.print("Введите поисковый запрос: ");
        String searchTerm = scanner.nextLine();
        try {
            List<Student> students = studentService.searchStudents(searchTerm);
            if (students.isEmpty()) {
                System.out.println("Студенты не найдены по запросу: " + searchTerm);
            } else {
                students.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при поиске студентов: " + e.getMessage());
        }
    }

    private Long getValidatedLong() {
        while (true) {
            if (!scanner.hasNextLong()) {
                System.out.print("Введите корректное число: ");
                scanner.nextLine();
            } else {
                Long value = scanner.nextLong();
                scanner.nextLine(); 
                return value;
            }
        }
    }
}