package at.breitenfellner.roomquestions.ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.VotedQuestion;
import at.breitenfellner.roomquestions.state.QuestionsViewModel;
import timber.log.Timber;

/**
 * Created by helmuth on 19/08/2017.
 */

public class QuestionsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    QuestionsViewModel viewModel;

    public QuestionsRemoteViewsFactory(Context context, String roomKey) {
        this.context = context;
        viewModel = RoomWidget.getViewModel(roomKey);
    }

    @Override
    public void onCreate() {
        //
    }

    @Override
    public void onDataSetChanged() {
        //
    }

    @Override
    public void onDestroy() {
        //
    }

    @Override
    public int getCount() {
        List<VotedQuestion> questions = viewModel.getLiveQuestions().getValue();
        if (questions != null) {
            Timber.d("Question count: ", questions.size());
            return questions.size();
        }
        Timber.d("Question count: 0 - no questions found!");
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.item_question);
        remoteView.setOnClickFillInIntent(R.id.item_question_layout, new Intent());
        List<VotedQuestion> questions = viewModel.getLiveQuestions().getValue();
        if (questions != null && questions.size() > i) {
            VotedQuestion question = questions.get(i);
            remoteView.setTextViewText(R.id.star_count, Integer.toString(question.voteCount));
            remoteView.setTextViewText(R.id.question_author, question.author);
            remoteView.setTextViewText(R.id.question_text, question.text);
            remoteView.setImageViewResource(R.id.star_image, R.drawable.ic_star_empty);
            remoteView.setBoolean(R.id.star_image, "setEnabled", false);
        }
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
