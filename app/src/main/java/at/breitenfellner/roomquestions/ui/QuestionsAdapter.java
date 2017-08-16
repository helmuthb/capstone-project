package at.breitenfellner.roomquestions.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.VotedQuestion;
import at.breitenfellner.roomquestions.util.KeyIdSource;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter to show questions in a recycler view.
 */

class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private List<VotedQuestion> questions;
    private QuestionsListCallback listCallback;
    private QuestionClickListener listener;
    private KeyIdSource keyIdSource;

    QuestionsAdapter(QuestionClickListener listener, KeyIdSource keyIdSource) {
        this.listCallback = new QuestionsListCallback(this);
        this.questions = new ArrayList<VotedQuestion>();
        this.listener = listener;
        this.keyIdSource = keyIdSource;
        setHasStableIds(true);
    }

    void updateQuestionsList(List<VotedQuestion> newQuestions) {
        DiffUtilCallback callback = new DiffUtilCallback(questions, newQuestions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        questions = newQuestions;
        result.dispatchUpdatesTo(this);
    }

    @Override
    public long getItemId(int position) {
        return keyIdSource.getId(questions.get(position).key);
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
        VotedQuestion question = questions.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        VotedQuestion question;
        @BindView(R.id.star_combined)
        View voteStar;
        @BindView(R.id.star_count)
        TextView voteCount;
        @BindView(R.id.star_image)
        ImageView voteImage;
        @BindView(R.id.question_text)
        TextView questionText;
        @BindView(R.id.question_author)
        TextView questionAuthor;

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

        void bind(VotedQuestion question) {
            this.question = question;
            if (question != null) {
                questionText.setText(question.text);
                questionAuthor.setText(question.author);
                voteCount.setText(Integer.toString(question.voteCount));
                if (question.votedByMe) {
                    voteImage.setImageResource(R.drawable.ic_star_full);
                }
                else {
                    voteImage.setImageResource(R.drawable.ic_star_empty);
                }
            }
        }
    }


    static boolean areContentsTheSame(VotedQuestion oldItem, VotedQuestion newItem) {
        if (oldItem == null || newItem == null) {
            return oldItem == newItem;
        }
        return oldItem.votedByMe == newItem.votedByMe
                && oldItem.voteCount == newItem.voteCount
                && oldItem.author == newItem.author
                && oldItem.date == newItem.date
                && oldItem.text == newItem.text;
    }

    static boolean areItemsTheSame(VotedQuestion item1, VotedQuestion item2) {
        if (item1 == null || item2 == null) {
            return item1 == item2;
        }
        return item1.key.equals(item2.key);
    }

    static class DiffUtilCallback extends DiffUtil.Callback {
        private List<VotedQuestion> oldList;
        private List<VotedQuestion> newList;

        DiffUtilCallback(List<VotedQuestion> oldList, List<VotedQuestion> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            VotedQuestion oldQuestion = oldList.get(oldItemPosition);
            VotedQuestion newQuestion = newList.get(newItemPosition);
            return QuestionsAdapter.areItemsTheSame(oldQuestion, newQuestion);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            VotedQuestion oldQuestion = oldList.get(oldItemPosition);
            VotedQuestion newQuestion = newList.get(newItemPosition);
            return QuestionsAdapter.areContentsTheSame(oldQuestion, newQuestion);
        }
        //
    }

    static class QuestionsListCallback extends SortedListAdapterCallback<VotedQuestion> {
        private VotedQuestion.Comparator comparator;

        QuestionsListCallback(QuestionsAdapter adapter) {
            super(adapter);
            comparator = new VotedQuestion.Comparator();
        }

        @Override
        public int compare(VotedQuestion o1, VotedQuestion o2) {
            return comparator.compare(o1, o2);
        }

        @Override
        public boolean areContentsTheSame(VotedQuestion oldItem, VotedQuestion newItem) {
            return QuestionsAdapter.areContentsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areItemsTheSame(VotedQuestion item1, VotedQuestion item2) {
            return QuestionsAdapter.areItemsTheSame(item1, item2);
        }
    }

    interface QuestionClickListener {
        void onQuestionClicked(VotedQuestion question);
    }
}
