package com.example.stomabooking.managers;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.stomabooking.models.Appointment;
import com.example.stomabooking.models.AppointmentImage;
import com.example.stomabooking.models.ChatMessage;
import com.example.stomabooking.models.ChatRoom;
import com.example.stomabooking.models.Doctor;
import com.example.stomabooking.models.Service;
import com.example.stomabooking.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class DatabaseManager {
    public interface Callback {
        void finishLoadingTables();
    }

    private static final DatabaseManager mInstance = new DatabaseManager();

    private int tablesReady = 0;
    private final DatabaseReference userTableReference;
    private DataSnapshot userTableDataSnapshot;
    private final DatabaseReference appointmentTableReference;
    private DataSnapshot appointmentTableDataSnapshot;
    private final DatabaseReference appointmentImageTableReference;
    private DataSnapshot appointmentImageTableDataSnapshot;
    private final DatabaseReference doctorTableReference;
    private DataSnapshot doctorTableDataSnapshot;
    private final DatabaseReference serviceTableReference;
    private DataSnapshot serviceTableDataSnapshot;
    private final DatabaseReference chatRoomTableReference;
    private DataSnapshot chatRoomTableDataSnapshot;
    private final DatabaseReference chatMessageTableReference;
    private DataSnapshot chatMessageTableDataSnapshot;

    private DatabaseManager() {
        userTableReference = FirebaseDatabase.getInstance().getReference("user_table");
        appointmentTableReference = FirebaseDatabase.getInstance().getReference("appointment_table");
        appointmentImageTableReference = FirebaseDatabase.getInstance().getReference("appointment_image_table");
        doctorTableReference = FirebaseDatabase.getInstance().getReference("doctor_table");
        serviceTableReference = FirebaseDatabase.getInstance().getReference("service_table");
        chatRoomTableReference = FirebaseDatabase.getInstance().getReference("chat_room_table");
        chatMessageTableReference = FirebaseDatabase.getInstance().getReference("chat_message_table");
    }

    public static DatabaseManager getInstance() {
        return mInstance;
    }

    public void init(DatabaseManager.Callback callback) {
        userTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userTableDataSnapshot = dataSnapshot;
                tablesReady++;
                if (tablesReady == 7) {
                    callback.finishLoadingTables();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        appointmentTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentTableDataSnapshot = dataSnapshot;
                tablesReady++;
                if (tablesReady == 7) {
                    callback.finishLoadingTables();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        appointmentImageTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentImageTableDataSnapshot = dataSnapshot;
                tablesReady++;
                if (tablesReady == 7) {
                    callback.finishLoadingTables();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        doctorTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorTableDataSnapshot = dataSnapshot;
                tablesReady++;
                if (tablesReady == 7) {
                    callback.finishLoadingTables();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        serviceTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceTableDataSnapshot = dataSnapshot;
                tablesReady++;
                if (tablesReady == 7) {
                    callback.finishLoadingTables();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        chatRoomTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRoomTableDataSnapshot = dataSnapshot;
                tablesReady++;
                if (tablesReady == 7) {
                    callback.finishLoadingTables();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        chatMessageTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessageTableDataSnapshot = dataSnapshot;
                tablesReady++;
                if (tablesReady == 7) {
                    callback.finishLoadingTables();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void addUser(User user) {
        userTableReference.child(user.id).setValue(user);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        for (DataSnapshot node : userTableDataSnapshot.getChildren()) {
            String emailNode = node.child("email").getValue(String.class);
            String passwordNode = node.child("password").getValue(String.class);
            if(emailNode != null && emailNode.equals(email) && passwordNode != null && passwordNode.equals(password)) {
                return node.getValue(User.class);
            }
        }
        return null;
    }

    public ArrayList<User> getAllAssociatedUsers(String parentId) {
        ArrayList<User> usersList = new ArrayList<>();
        for (DataSnapshot node : userTableDataSnapshot.getChildren()) {
            String parentIdNode = node.child("parent_id").getValue(String.class);
            if(parentIdNode != null && parentIdNode.equals(parentId)) {
                usersList.add(node.getValue(User.class));
            }
        }
        usersList.sort(Comparator.comparing(user -> user.firstName));
        return usersList;
    }

    public void removeUser(User user) {
        userTableReference.child(user.id).removeValue();
    }

    public void addAppointment(Appointment appointment) {
        appointmentTableReference.child(appointment.id).setValue(appointment);
    }

    public ArrayList<Appointment> getAppointmentsByDateForUser(String userId, LocalDate date) {
        ArrayList<Appointment> appointmentsList = new ArrayList<>();
        String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        for (DataSnapshot node : appointmentTableDataSnapshot.getChildren()) {
            String userIdNode = node.child("user_id").getValue(String.class);
            String dateNode = node.child("date").getValue(String.class);
            if(userIdNode != null && userIdNode.equals(userId) && dateNode != null && dateNode.equals(dateString)) {
                Appointment appointment = node.getValue(Appointment.class);
                ArrayList<AppointmentImage> images = getAppointmentImagesByAppointmentId(appointment.id);
                appointment.images.addAll(images);
                appointmentsList.add(appointment);
            }
        }
        appointmentsList.sort(Comparator.comparing(appointment -> appointment.startHour));
        return appointmentsList;
    }

    public ArrayList<Appointment> getAppointmentsByDateForDoctor(String doctorId, LocalDate date) {
        ArrayList<Appointment> appointmentsList = new ArrayList<>();
        String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        for (DataSnapshot node : appointmentTableDataSnapshot.getChildren()) {
            String doctorIdNode = node.child("doctor_id").getValue(String.class);
            String dateNode = node.child("date").getValue(String.class);
            if(doctorIdNode != null && doctorIdNode.equals(doctorId) && dateNode != null && dateNode.equals(dateString)) {
                Appointment appointment = node.getValue(Appointment.class);
                ArrayList<AppointmentImage> images = getAppointmentImagesByAppointmentId(appointment.id);
                appointment.images.addAll(images);
                appointmentsList.add(appointment);
            }
        }
        appointmentsList.sort(Comparator.comparing(appointment -> appointment.startHour));
        return appointmentsList;
    }

    public ArrayList<Appointment> getAppointmentsHistory(String userId) {
        ArrayList<Appointment> appointmentsList = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (DataSnapshot node : appointmentTableDataSnapshot.getChildren()) {
            String userIdNode = node.child("user_id").getValue(String.class);
            String dateNode = node.child("date").getValue(String.class);

            if(userIdNode != null && userIdNode.equals(userId) && dateNode != null) {
                LocalDate date = LocalDate.parse(dateNode, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                if (date.isBefore(now)) {
                    Appointment appointment = node.getValue(Appointment.class);
                    ArrayList<AppointmentImage> images = getAppointmentImagesByAppointmentId(appointment.id);
                    appointment.images.addAll(images);
                    appointmentsList.add(appointment);
                }
            }
        }

        appointmentsList.sort((appointment1, appointment2) -> {
            LocalDate date1 = LocalDate.parse(appointment1.date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            LocalDate date2 = LocalDate.parse(appointment2.date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if (date1.isBefore(date2)) {
                return 1;
            }
            if (date1.isAfter(date2)) {
                return -1;
            }
            return appointment2.startHour.compareTo(appointment1.endHour);
        });
        return appointmentsList;
    }

    public ArrayList<String> getAppointmentAvailableHours(String doctorName, LocalDate date, int appointmentDuration) {
        ArrayList<Appointment> appointmentsList = new ArrayList<>();
        ArrayList<LocalTime> appointmentHoursList = new ArrayList<>();
        ArrayList<String> hoursList = new ArrayList<>();
        String dateString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        for (DataSnapshot node : appointmentTableDataSnapshot.getChildren()) {
            String doctorNameNode = node.child("doctor_name").getValue(String.class);
            String dateNode = node.child("date").getValue(String.class);
            if(doctorNameNode != null && doctorNameNode.equals(doctorName) && dateNode != null && dateNode.equals(dateString)) {
                appointmentsList.add(node.getValue(Appointment.class));
            }
        }
        appointmentsList.sort(Comparator.comparing(appointment -> appointment.startHour));
        // appointmentHoursList este o lista care va tine orele de start si de final ale programariolor deja existente
        // [ startHour, endHour, startHour, endHour, ... ]
        for (Appointment appointment : appointmentsList) {
            appointmentHoursList.add(LocalTime.parse(appointment.startHour, DateTimeFormatter.ofPattern("HH:mm")));
            appointmentHoursList.add(LocalTime.parse(appointment.endHour, DateTimeFormatter.ofPattern("HH:mm")));
        }

        int index = 0;
        LocalTime time = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        // Cat timp ora de start a programarii pe care vreau sa o fac + durata programarii
        // este mai mica sau egala decat ora de final de program
        // Daca nu mai sunt elemente de verificat in lista cu orele programarilor deja existente opresc aceste verificari
        while (time.plusMinutes(appointmentDuration).compareTo(endTime) <= 0 && index < appointmentHoursList.size()) {
            // Daca ora de start a programarii pe care vreau sa o fac + durata programarii
            // este mai mica sau egala decat ora de start a programarii deja existente
            if (time.plusMinutes(appointmentDuration).compareTo(appointmentHoursList.get(index)) <= 0) {
                // Atunci adaug ora de start a programarii si o cresc cu 30 de minute
                hoursList.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                time = time.plusMinutes(30);
            } else {
                // Altfel setez ora de start a programarii cu ora de final a programarii deja existente
                index++;
                time = appointmentHoursList.get(index);
                // Cresc index-ul pt a ma muta in lista la ora de start a programarii urmatoare deja existente
                index++;
            }
        }
        // Continui sa adaug ore pana la finalul programului
        while (time.plusMinutes(appointmentDuration).compareTo(endTime) <= 0) {
            hoursList.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            time = time.plusMinutes(30);
        }

        return hoursList;
    }

    public void removeAppointment(Appointment appointment) {
        ArrayList<AppointmentImage> appointmentImagesList = getAppointmentImagesByAppointmentId(appointment.id);
        for (AppointmentImage appointmentImage : appointmentImagesList) {
            removeAppointmentImage(appointmentImage);
        }
        appointmentTableReference.child(appointment.id).removeValue();
    }

    public void addAppointmentImage(String appointmentId, Uri photoUri) {
        String id = UUID.randomUUID().toString();
        StorageReference imageReference = FirebaseStorage.getInstance().getReference().child("images/" + id + ".jpg");
        imageReference.putFile(photoUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AppointmentImage appointmentImage = new AppointmentImage(id, appointmentId, task.getResult().toString());
                        appointmentImageTableReference.child(appointmentImage.id).setValue(appointmentImage);
                    }
                });
    }

    private ArrayList<AppointmentImage> getAppointmentImagesByAppointmentId(String appointmentId) {
        ArrayList<AppointmentImage> appointmentImagesList = new ArrayList<>();
        for (DataSnapshot node : appointmentImageTableDataSnapshot.getChildren()) {
            String appointmentIdNode = node.child("appointment_id").getValue(String.class);
            if(appointmentIdNode != null && appointmentIdNode.equals(appointmentId)) {
                appointmentImagesList.add(node.getValue(AppointmentImage.class));
            }
        }
        return appointmentImagesList;
    }

    private void removeAppointmentImage(AppointmentImage appointmentImage) {
        FirebaseStorage.getInstance().getReference()
                .child("images/" + appointmentImage.id + ".jpg")
                .delete().addOnSuccessListener(aVoid -> {
                    appointmentImageTableReference.child(appointmentImage.id).removeValue();
                });
    }

    public Doctor getDoctorByEmailAndPassword(String email, String password) {
        for (DataSnapshot node : doctorTableDataSnapshot.getChildren()) {
            String emailNode = node.child("email").getValue(String.class);
            String passwordNode = node.child("password").getValue(String.class);
            if(emailNode != null && emailNode.equals(email) && passwordNode != null && passwordNode.equals(password)) {
                return node.getValue(Doctor.class);
            }
        }
        return null;
    }

    public ArrayList<Doctor> getAllDoctors() {
        ArrayList<Doctor> doctorsList = new ArrayList<>();
        for (DataSnapshot node : doctorTableDataSnapshot.getChildren()) {
            doctorsList.add(node.getValue(Doctor.class));
        }
        doctorsList.sort(Comparator.comparing(doctor -> doctor.name));
        return doctorsList;
    }

    public ArrayList<Service> getAllServices() {
        ArrayList<Service> defaultAppointmentsList = new ArrayList<>();
        for (DataSnapshot node : serviceTableDataSnapshot.getChildren()) {
            defaultAppointmentsList.add(node.getValue(Service.class));
        }
        defaultAppointmentsList.sort(Comparator.comparing(service -> service.title));
        return defaultAppointmentsList;
    }

    public void addChatRoom(ChatRoom chatRoom) {
        chatRoomTableReference.child(chatRoom.id).setValue(chatRoom);
    }

    public ArrayList<ChatRoom> getChatRoomsForUser(String userId) {
        ArrayList<ChatRoom> chatRoomsList = new ArrayList<>();
        for (DataSnapshot node : chatRoomTableDataSnapshot.getChildren()) {
            String userIdNode = node.child("user_id").getValue(String.class);
            if(userIdNode != null && userIdNode.equals(userId)) {
                chatRoomsList.add(node.getValue(ChatRoom.class));
            }
        }
        chatRoomsList.sort(Comparator.comparing(chatRoom -> chatRoom.doctorName));
        return chatRoomsList;
    }

    public ArrayList<ChatRoom> getChatRoomsForDoctor(String doctorId) {
        ArrayList<ChatRoom> chatRoomsList = new ArrayList<>();
        for (DataSnapshot node : chatRoomTableDataSnapshot.getChildren()) {
            String doctorIdNode = node.child("doctor_id").getValue(String.class);
            if(doctorIdNode != null && doctorIdNode.equals(doctorId)) {
                chatRoomsList.add(node.getValue(ChatRoom.class));
            }
        }
        chatRoomsList.sort(Comparator.comparing(chatRoom -> chatRoom.userName));
        return chatRoomsList;
    }

    public void addChatMessage(ChatMessage chatMessage) {
        chatMessageTableReference.child(chatMessage.id).setValue(chatMessage);
    }

    public ArrayList<ChatMessage> getChatMessagesByRoomId(String roomId) {
        ArrayList<ChatMessage> chatMessagesList = new ArrayList<>();
        for (DataSnapshot node : chatMessageTableDataSnapshot.getChildren()) {
            String roomIdNode = node.child("room_id").getValue(String.class);
            if(roomIdNode != null && roomIdNode.equals(roomId)) {
                chatMessagesList.add(node.getValue(ChatMessage.class));
            }
        }
        chatMessagesList.sort(Comparator.comparing(chatRoom -> chatRoom.timestamp));
        return chatMessagesList;
    }
}
