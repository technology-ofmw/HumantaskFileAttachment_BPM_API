# Handling Humantask files attachment using HWF/BPM APIs
Handling Humantask files attachment using HWF/BPM APIs

This Application projects developed under the following environment.
Jdeveloper: 11.1.1.7
Oracle SOA Suite 11.1.1.7

A asynchronous BPM process have a simple Humantask, implemented and deployed. Using HWF/BPM APIs, get the task (Task). From the task get the file attachement as a InputStream. Using the InputStream create a File, we can store it your own File management system(Documentum, Alfresco etc).

This approach is for non-UCM file management storage type. For better understanding of Humantask and File attachement refer this [post](https://blogs.oracle.com/bpmtech/entry/handling_humantask_attachments_in_oracle).

Finally APPROVE the task to complete the task instance.