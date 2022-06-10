package swdm2016.gachon.mr_demo.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import swdm2016.gachon.mr_demo.R;
import swdm2016.gachon.mr_demo.Activity.WakeChallengeActivity;

public class ChallengeFragment extends Fragment {
private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_challenge, container, false);

        Button wakeChalBtn = view.findViewById(R.id.wakeChall);
        wakeChalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WakeChallengeActivity.class);
                startActivity(intent);
            }
        });

        // 1만보 걷기 페이지로 가기
//        Button walkChalBtn = view.findViewById(R.id.walkChall);
//        walkChalBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), WalkChallengeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // 2키로 감량하기 페이지로 가기
//        Button weightChalBtn = view.findViewById(R.id.weightChall);
//        weightChalBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), WeightChallengeActivity.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }
}