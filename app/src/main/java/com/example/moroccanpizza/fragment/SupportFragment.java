package com.example.moroccanpizza.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moroccanpizza.R;
import com.google.android.material.card.MaterialCardView;

public class SupportFragment extends Fragment {

    private MaterialCardView callSupportCard;
    private MaterialCardView emailSupportCard;
    private MaterialCardView chatSupportCard;
    private MaterialCardView faqCard;
    private EditText messageEditText;
    private Button sendMessageButton;
    private Button backButton;

    private static final String SUPPORT_PHONE = "+212 5XX-XXX-XXX";
    private static final String SUPPORT_EMAIL = "support@moroccanpizza.com";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        
        // Initialize views
        callSupportCard = view.findViewById(R.id.call_support_card);
        emailSupportCard = view.findViewById(R.id.email_support_card);
        chatSupportCard = view.findViewById(R.id.chat_support_card);
        faqCard = view.findViewById(R.id.faq_card);
        messageEditText = view.findViewById(R.id.support_message);
        sendMessageButton = view.findViewById(R.id.send_message_button);
        backButton = view.findViewById(R.id.support_back_button);
        
        // Setup click listeners
        setupClickListeners();
        
        return view;
    }

    private void setupClickListeners() {
        callSupportCard.setOnClickListener(v -> {
            // Call support
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + SUPPORT_PHONE));
            startActivity(intent);
        });
        
        emailSupportCard.setOnClickListener(v -> {
            // Email support
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Customer Support Request");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });
        
        chatSupportCard.setOnClickListener(v -> {
            // Open chat support
            // In a real app, you would open a chat interface
            // For demo, just show the message input
            messageEditText.setVisibility(View.VISIBLE);
            sendMessageButton.setVisibility(View.VISIBLE);
        });
        
        faqCard.setOnClickListener(v -> {
            // Open FAQ section
            // In a real app, you would navigate to a FAQ page
            // For demo, just show a message
            Toast.makeText(getContext(), "FAQ section will be available soon!", Toast.LENGTH_SHORT).show();
        });
        
        sendMessageButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (message.isEmpty()) {
                messageEditText.setError("Please enter a message");
                return;
            }
            
            // Send message to support
            // In a real app, you would send this to your support system
            // For demo, just show a confirmation
            Toast.makeText(getContext(), "Message sent to support team", Toast.LENGTH_SHORT).show();
            messageEditText.setText("");
            messageEditText.setVisibility(View.GONE);
            sendMessageButton.setVisibility(View.GONE);
        });
        
        backButton.setOnClickListener(v -> {
            // Navigate back
            requireActivity().onBackPressed();
        });
    }
}
