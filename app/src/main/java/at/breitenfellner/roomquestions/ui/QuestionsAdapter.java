package at.breitenfellner.roomquestions.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Question;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter to show questions in a recycler view.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private List<Question> questions;
    private QuestionClickListener listener;

    QuestionsAdapter(List<Question> questions, QuestionClickListener listener) {
        this.questions = questions;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Question question;
        @BindView(R.id.star_combined)
        View voteStar;
        @BindView(R.id.question_text)
        TextView questionText;

        ViewHolder(@NonNull View itemView, final QuestionClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // add click listener to vote-star
            voteStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onQuestionClicked(question);
                }
            });
        }

        void bind(Question question) {
            this.question = question;
            if (question != null) {
                questionText.setText(question.text);
            }
        }
    }

    interface QuestionClickListener {
        void onQuestionClicked(Question question);
    }
}
