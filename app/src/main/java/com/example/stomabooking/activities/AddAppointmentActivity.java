package com.example.stomabooking.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.BuildConfig;
import com.example.stomabooking.R;
import com.example.stomabooking.adapters.PhotoAdapter;
import com.example.stomabooking.adapters.SelectableTextAdapter;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.Appointment;
import com.example.stomabooking.models.Doctor;
import com.example.stomabooking.models.SelectableText;
import com.example.stomabooking.models.Service;
import com.example.stomabooking.models.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddAppointmentActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 101;

    private ImageButton backIb;
    private TextView patientNameTv;
    private TextView doctorNameTv;
    private TextView serviceTv;
    private TextView dateTv;
    private ImageView addPhotoIv;
    private Button addAppointmentBtn;

    private AlertDialog dialog;

    private final ArrayList<User> usersList = new ArrayList<>();
    private User selectedPatient;
    private final ArrayList<Doctor> doctorsList = new ArrayList<>();
    private Doctor selectedDoctor = null;
    private final ArrayList<Service> servicesList = new ArrayList<>();
    private Service selectedService = null;
    private LocalDate selectedDate = LocalDate.now();
    private final ArrayList<SelectableText> hoursList = new ArrayList<>();
    private SelectableTextAdapter hoursAdapter;
    private String selectedHour = null;
    private final ArrayList<Uri> photoUriList = new ArrayList<>();
    private PhotoAdapter photoAdapter;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    addPhotoIv.callOnClick();
                }
            });

    private Uri tempPhotoUri = null;
    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
        if (success && tempPhotoUri != null) {
            photoUriList.add(tempPhotoUri);
            photoAdapter.notifyItemInserted(photoUriList.size() - 1);
        }
    });
    private final ActivityResultLauncher<String> choosePictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            photoUriList.add(uri);
            photoAdapter.notifyItemInserted(photoUriList.size() - 1);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        initViews();
        setClickListeners();
        setData();
    }

    private void initViews() {
        backIb = findViewById(R.id.ibBack);
        patientNameTv = findViewById(R.id.tvPatientName);
        doctorNameTv = findViewById(R.id.tvDoctorName);
        serviceTv = findViewById(R.id.tvService);
        dateTv = findViewById(R.id.tvDate);
        addPhotoIv = findViewById(R.id.ivAddPhoto);
        addAppointmentBtn = findViewById(R.id.btnAddAppointment);

        RecyclerView hoursRv = findViewById(R.id.rvHours);
        hoursAdapter = new SelectableTextAdapter(hoursList, selectedItem -> {
            selectedHour = selectedItem.getText();
            for (int i = 0; i < hoursList.size(); i++) {
                SelectableText selectableText = hoursList.get(i);
                boolean isSelected = selectableText.getText().equals(selectedItem.getText());
                if (selectableText.isSelected() != isSelected) {
                    hoursAdapter.notifyItemChanged(i);
                    selectableText.setSelected(isSelected);
                }
            }
        });
        hoursRv.setAdapter(hoursAdapter);

        RecyclerView photoRv = findViewById(R.id.rvPhotos);
        photoAdapter = new PhotoAdapter(photoUriList, position -> {
            photoUriList.remove(position);
            photoAdapter.notifyItemRemoved(position);
        });
        photoRv.setAdapter(photoAdapter);
    }

    private void setClickListeners() {
        backIb.setOnClickListener(v -> onBackPressed());

        patientNameTv.setOnClickListener(v -> {
            showListDialog(usersList.stream().map(user -> user.firstName + " " + user.lastName).collect(Collectors.toList()),
                    (parent, view, position, id) -> {
                        selectedPatient = usersList.get(position);
                        patientNameTv.setText(selectedPatient.firstName + " " + selectedPatient.lastName);
                        dialog.dismiss();
                    });
        });

        doctorNameTv.setOnClickListener(v -> {
            showListDialog(doctorsList.stream().map(doctor -> doctor.name).collect(Collectors.toList()),
                    (parent, view, position, id) -> {
                        selectedDoctor = doctorsList.get(position);
                        doctorNameTv.setText(selectedDoctor.name);
                        dialog.dismiss();
                        setAvailableHours();
                    });
        });

        serviceTv.setOnClickListener(v -> {
            showListDialog(servicesList.stream().map(service -> service.title).collect(Collectors.toList()),
                    (parent, view, position, id) -> {
                        selectedService = servicesList.get(position);
                        serviceTv.setText(selectedService.title);
                        dialog.dismiss();
                        setAvailableHours();
                    });
        });

        dateTv.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    R.style.DialogTheme,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                        dateTv.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                        setAvailableHours();
                    },
                    selectedDate.getYear(),
                    selectedDate.getMonthValue() - 1,
                    selectedDate.getDayOfMonth()
            );
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
            datePickerDialog.updateDate(selectedDate.getYear(), selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.pink));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.pink));
        });

        addPhotoIv.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                return;
            }

            List<String> list = new ArrayList<>();
            list.add(getString(R.string.take_picture));
            list.add(getString(R.string.choose_from_gallery));

            showListDialog(list, (parent, view, position, id) -> {
                if (position == 0) {
                    tempPhotoUri = getTempFileUri();
                    cameraLauncher.launch(tempPhotoUri);
                } else {
                    choosePictureLauncher.launch("image/*");
                }
                dialog.dismiss();
            });
        });

        addAppointmentBtn.setOnClickListener(v -> {
            if (!isDataValid()) {
                return;
            }

            String appointmentId = UUID.randomUUID().toString();
            String dateString = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            LocalDateTime localDateTime = LocalDateTime.of(selectedDate, LocalTime.parse(selectedHour, DateTimeFormatter.ofPattern("HH:mm")));
            String endHour = localDateTime.plusMinutes(selectedService.duration).format(DateTimeFormatter.ofPattern("HH:mm"));

            for (Uri photoUri : photoUriList) {
                DatabaseManager.getInstance().addAppointmentImage(appointmentId, photoUri);
            }
            DatabaseManager.getInstance().addAppointment(new Appointment(appointmentId, UserManager.getInstance().getCurrentUser().id,
                    selectedPatient.firstName + " " + selectedPatient.lastName, selectedDoctor.id, selectedDoctor.name, dateString, selectedHour,
                    endHour, selectedService.title, selectedService.duration, selectedService.price));

            setResult(RESULT_OK);
            finish();
        });
    }

    private void setData() {
        User user = UserManager.getInstance().getCurrentUser();
        selectedPatient = user;
        patientNameTv.setText(user.firstName + " " + user.lastName);
        dateTv.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        usersList.add(user);
        usersList.addAll(DatabaseManager.getInstance().getAllAssociatedUsers(user.id));
        doctorsList.addAll(DatabaseManager.getInstance().getAllDoctors());
        servicesList.addAll(DatabaseManager.getInstance().getAllServices());
    }

    private void showListDialog(List<String> list, AdapterView.OnItemClickListener itemClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View rowList = getLayoutInflater().inflate(R.layout.dialog_list, null);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        ListView listView = rowList.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);

        adapter.notifyDataSetChanged();

        alertDialog.setView(rowList);
        dialog = alertDialog.create();
        dialog.show();
    }

    private void setAvailableHours() {
        if (selectedDoctor == null || selectedService == null) {
            return;
        }

        int size = hoursList.size();
        hoursList.clear();
        hoursAdapter.notifyItemRangeRemoved(0, size);

        ArrayList<String> availableHours = DatabaseManager.getInstance()
                .getAppointmentAvailableHours(selectedDoctor.name, selectedDate, selectedService.duration);
        for (String startHour : availableHours) {
            hoursList.add(new SelectableText(startHour));
        }
        hoursAdapter.notifyItemRangeInserted(0, hoursList.size());
    }

    private Uri getTempFileUri() {
        try {
            File tmpFile = File.createTempFile("temp_image_file", ".png");
            tmpFile.createNewFile();
            tmpFile.deleteOnExit();
            return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", tmpFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isDataValid() {
        if (selectedDoctor == null) {
            Toast.makeText(this, R.string.error_choose_doctor_for_appointment, Toast.LENGTH_LONG).show();
            return false;
        }
        if (selectedService == null) {
            Toast.makeText(this, R.string.error_choose_service_for_appointment, Toast.LENGTH_LONG).show();
            return false;
        }
        if (selectedHour == null) {
            Toast.makeText(this, R.string.error_choose_hour_for_appointment, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}