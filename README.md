# StudentManagement

Small project for testing the usage of Jetty, Jersey and Java 8 (Streams, lambda). Web service with the following functionality:
  - retrieve the list of students
  - retrieve the student with the specified id
  - retrieve the grades of the student with the specified id
  - retrieve the students sorted in descending order by ther average grades
  - retrieve the list of students that have a certain grade at a certain topic
  - add a new student (a student with an already existing name cannot be added)
  - add a new grade (needs to specify the name of an existing student)
  - delete a student (corresponding grades will also be deleted).
