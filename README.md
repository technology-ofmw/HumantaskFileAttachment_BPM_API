# Handling Humantask files attachment using HWF/BPM APIs
Handling Humantask files attachment using HWF/BPM APIs

 1. A asynchronous BPM process have a simple Humantask, implemented and deployed. From the Oracle Fusion Middleware control test (initiate/create) task instance. Login to workspace to perform the task, and fill the form and attache the files.
 2. Using HWF/BPM APIs, get the task (Task). From the task get the file attachment as a InputStream. Using the InputStream create a File to local or you can store it your own File management system(Documentum, Alfresco etc).
 3. This method is for non-UCM file management storage type. For better understanding of Humantask and File attachement refer this [post](https://blogs.oracle.com/bpmtech/entry/handling_humantask_attachments_in_oracle).
 4. Finally ‘APPROVE’ the task(update task outcome) to complete the task instance.

