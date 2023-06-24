package com.example.stomabooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.adapters.ChatMessageAdapter;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.ChatMessage;
import com.example.stomabooking.models.ChatRoom;
import com.example.stomabooking.models.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;

public class ChatMessagesActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 102;

    public static final String CHAT_ROOM_ID = "chat_room_id";
    public static final String USER_NAME = "user_name";
    public static final String DOCTOR_ID = "doctor_id";
    public static final String DOCTOR_NAME = "doctor_name";


    private ImageButton backIb;
    private RecyclerView chatRoomsRv;
    private TextView emptyChatMessagesTv;
    private EditText messageEt;
    private ImageButton sendIb;

    private final ArrayList<ChatMessage> chatMessagesList = new ArrayList<>();
    private ChatMessageAdapter chatMessageAdapter;

    private String chatRoomId;
    private String userName;
    private String doctorId;
    private String doctorName;
    private boolean shouldUpdateRoomsList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        chatRoomId = getIntent().getStringExtra(CHAT_ROOM_ID);
        userName = getIntent().getStringExtra(USER_NAME);
        doctorId = getIntent().getStringExtra(DOCTOR_ID);
        doctorName = getIntent().getStringExtra(DOCTOR_NAME);

        initViews();
        setClickListeners();
        setData();
    }

    @Override
    public void onBackPressed() {
        if (shouldUpdateRoomsList) {
            Intent intent = getIntent();
            intent.putExtra(CHAT_ROOM_ID, chatRoomId);
            intent.putExtra(DOCTOR_ID, doctorId);
            intent.putExtra(DOCTOR_NAME, doctorName);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void initViews() {
        backIb = findViewById(R.id.ibBack);
        emptyChatMessagesTv = findViewById(R.id.tvEmptyChatMessages);
        messageEt = findViewById(R.id.etMessage);
        messageEt.setOnTouchListener((v, event) -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatRoomsRv.scrollToPosition(chatMessagesList.size() - 1);
                }
            }, 300);
            return false;
        });
        sendIb = findViewById(R.id.ibSend);

        chatRoomsRv = findViewById(R.id.rvChatMessages);
        chatMessageAdapter = new ChatMessageAdapter(chatMessagesList);
        chatRoomsRv.setAdapter(chatMessageAdapter);
    }

    private void setClickListeners() {
        backIb.setOnClickListener(v -> onBackPressed());

        sendIb.setOnClickListener(v -> {
            String message = messageEt.getText().toString().trim();
            if (message.isEmpty()) {
                return;
            }

            if (chatRoomId == null) {
                chatRoomId = UUID.randomUUID().toString();
                User user = UserManager.getInstance().getCurrentUser();
                ChatRoom chatRoom = new ChatRoom(chatRoomId, user.id, user.firstName + " " + user.lastName, doctorId, doctorName);
                DatabaseManager.getInstance().addChatRoom(chatRoom);
                shouldUpdateRoomsList = true;
            }
            long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            boolean isFromDoctor = UserManager.getInstance().getCurrentUser() == null;
            ChatMessage chatMessage = new ChatMessage(UUID.randomUUID().toString(), chatRoomId, userName, doctorName, message, isFromDoctor, timestamp);
            DatabaseManager.getInstance().addChatMessage(chatMessage);

            emptyChatMessagesTv.setVisibility(View.GONE);
            chatMessagesList.add(chatMessage);
            chatMessageAdapter.notifyItemInserted(chatMessagesList.size() - 1);
            chatRoomsRv.scrollToPosition(chatMessagesList.size() - 1);
            messageEt.setText("");
        });
    }

    private void setData() {
        if (chatRoomId != null) {
            chatMessagesList.addAll(DatabaseManager.getInstance().getChatMessagesByRoomId(chatRoomId));
            chatMessageAdapter.notifyItemRangeInserted(0, chatMessagesList.size());
            chatRoomsRv.scrollToPosition(chatMessagesList.size() - 1);
        }

        emptyChatMessagesTv.setText(String.format(getString(R.string.empty_chat_messages), doctorName));
        emptyChatMessagesTv.setVisibility(chatMessagesList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}