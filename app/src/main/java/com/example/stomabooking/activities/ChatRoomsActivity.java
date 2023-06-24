package com.example.stomabooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.adapters.ChatRoomAdapter;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.ChatRoom;
import com.example.stomabooking.models.Doctor;
import com.example.stomabooking.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomsActivity extends AppCompatActivity {
    private ImageButton backIb;
    private TextView emptyChatRoomsTv;
    private FloatingActionButton newChatFab;

    private ArrayList<ChatRoom> chatRoomsList;
    private final ArrayList<String> chatDoctorsList = new ArrayList<>();
    private ChatRoomAdapter chatRoomAdapter;
    private AlertDialog dialog;
    private ArrayList<Doctor> doctorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);
        initViews();
        setClickListeners();
        doctorsList = DatabaseManager.getInstance().getAllDoctors();
        setData();

        if(UserManager.getInstance().getCurrentDoctor() != null) {
            emptyChatRoomsTv.setVisibility(View.GONE);
            newChatFab.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChatMessagesActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            String chatRoomId = data.getStringExtra(ChatMessagesActivity.CHAT_ROOM_ID);
            String doctorId = data.getStringExtra(ChatMessagesActivity.DOCTOR_ID);
            String doctorName = data.getStringExtra(ChatMessagesActivity.DOCTOR_NAME);

            User user = UserManager.getInstance().getCurrentUser();
            ChatRoom newChatRoom = new ChatRoom(chatRoomId, user.id, user.firstName + " " + user.lastName, doctorId, doctorName);
            chatRoomsList.add(newChatRoom);
            chatRoomsList.sort(Comparator.comparing(chatRoom -> chatRoom.doctorName));

            int index = chatRoomsList.indexOf(newChatRoom);
            chatDoctorsList.add(index, doctorName);
            chatRoomAdapter.notifyItemInserted(index);
            emptyChatRoomsTv.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        backIb = findViewById(R.id.ibBack);
        emptyChatRoomsTv = findViewById(R.id.tvEmptyChatRooms);
        newChatFab = findViewById(R.id.fabNewChat);

        RecyclerView chatRoomsRv = findViewById(R.id.rvChatRooms);
        chatRoomAdapter = new ChatRoomAdapter(chatDoctorsList, position -> {
            ChatRoom chatRoom = chatRoomsList.get(position);
            openChatMessages(chatRoom.id, chatRoom.userName, chatRoom.doctorId, chatRoom.doctorName);
        });
        chatRoomsRv.setAdapter(chatRoomAdapter);
    }

    private void setClickListeners() {
        backIb.setOnClickListener(v -> onBackPressed());

        newChatFab.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            View rowList = getLayoutInflater().inflate(R.layout.dialog_list, null);

            List<String> availableDoctorsList = doctorsList.stream().map(doctor -> doctor.name).collect(Collectors.toList());
            availableDoctorsList.removeAll(chatDoctorsList);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, availableDoctorsList);

            ListView listView = rowList.findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String doctorName = availableDoctorsList.get(position);
                Doctor doctor = null;
                for (Doctor doctorItem : doctorsList) {
                    if (doctorItem.name.equals(doctorName)) {
                        doctor = doctorItem;
                        break;
                    }
                }
                User user = UserManager.getInstance().getCurrentUser();
                openChatMessages(null, user.firstName + " " + user.lastName, doctor.id, doctorName);
                dialog.dismiss();
            });

            adapter.notifyDataSetChanged();

            alertDialog.setView(rowList);
            dialog = alertDialog.create();
            dialog.show();
        });
    }

    private void setData() {
        int size = chatDoctorsList.size();
        chatDoctorsList.clear();
        chatRoomAdapter.notifyItemRangeRemoved(0, size);

        if (UserManager.getInstance().getCurrentUser() != null) {
            chatRoomsList = DatabaseManager.getInstance().getChatRoomsForUser(UserManager.getInstance().getCurrentUser().id);
            chatDoctorsList.addAll(chatRoomsList.stream().map(item -> item.doctorName).collect(Collectors.toList()));
        } else {
            chatRoomsList = DatabaseManager.getInstance().getChatRoomsForDoctor(UserManager.getInstance().getCurrentDoctor().id);
            chatDoctorsList.addAll(chatRoomsList.stream().map(item -> item.userName).collect(Collectors.toList()));
        }
        chatRoomAdapter.notifyItemRangeInserted(0, chatDoctorsList.size());
        emptyChatRoomsTv.setVisibility(chatDoctorsList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void openChatMessages(String chatRoomId, String userName, String doctorId, String doctorName) {
        Intent intent = new Intent(this, ChatMessagesActivity.class);
        intent.putExtra(ChatMessagesActivity.CHAT_ROOM_ID, chatRoomId);
        intent.putExtra(ChatMessagesActivity.USER_NAME, userName);
        intent.putExtra(ChatMessagesActivity.DOCTOR_ID, doctorId);
        intent.putExtra(ChatMessagesActivity.DOCTOR_NAME, doctorName);
        startActivityForResult(intent, ChatMessagesActivity.REQUEST_CODE);
    }
}