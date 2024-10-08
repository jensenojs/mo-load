package io.mo.transaction;

import io.mo.para.PreparedPara;
import io.mo.thread.PreparedParaProducer;
import io.mo.transaction.ts.TSSQLScript;
import io.mo.util.ReplaceConfigUtil;

import java.util.ArrayList;

public class Transaction {
    private String name;
    private int theadnum;
    
    private String type = "common";

    private int mode = 0;

    private boolean prepared = false;

    

    private double sucrate = 1.0;
    
    private int waitTime = 0;
    private int interval = 0;

    private ArrayList<Integer> acceptErrorCodes = new ArrayList<>();

    private PreparedPara[] paras;

    private PreparedParaProducer preparedParaProducer;


    private String str_paras;

    private SQLScript script;

    public Transaction(String name,int theadnum){

        this.name = name;

        this.theadnum = theadnum;
    }

    public Transaction(String name,int theadnum,PreparedParaProducer preparedParaProducer){

        this.name = name;
        if (name.length() > 9)
            this.name = name.substring(0,9);

        this.theadnum = theadnum;
        this.preparedParaProducer = preparedParaProducer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTheadnum() {
        return theadnum;
    }

    public void setTheadnum(int theadnum) {
        this.theadnum = theadnum;
    }

    public SQLScript getScript() {
        return script;
    }

    public SQLScript createNewScript(){
        if( this.script instanceof TSSQLScript){
            return ((TSSQLScript) this.script).createNewScript();
        }
        
        SQLScript script = new SQLScript(this.script.length());
        for(int i = 0;i < script.length();i++){
            script.addCommand(this.script.getCommand(i));
        }
        ReplaceConfigUtil.replace(script);
        return script;
    }

    public void setScript(SQLScript script) {
//        if(script instanceof TSSQLScript) {
//            this.script = ((TSSQLScript) script).createNewScript();
//            return;
//        }
        
        this.script = script;
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public void parseParas(){
        String[] array = this.str_paras.split(",");
        paras = new PreparedPara[array.length];
        for(int i = 0;i < array.length;i++){
            if(array[i].startsWith("INT")){
                PreparedPara para = new PreparedPara("INT",array[i].substring(4,array[i].length()-1));

                paras[i] = para;
                preparedParaProducer.addPreparedPara(paras[i]);
            }

            if(array[i].startsWith("STR")){
                PreparedPara para = new PreparedPara("STR",array[i].substring(4,array[i].length()-1));
                paras[i] = para;
                preparedParaProducer.addPreparedPara(paras[i]);
            }
        }
    }

    public PreparedPara[] getParasList(){
        return this.paras;
    }

    public String getParas() {
        return str_paras;
    }

    public void setParas(String str_paras) {
        this.str_paras = str_paras;
    }

    public Transaction copy(){
        Transaction copy = new Transaction(this.getName(),this.getTheadnum(),this.preparedParaProducer);
        copy.setScript(this.getScript());
        copy.setPrepared(this.isPrepared());
        copy.setMode(this.getMode());
        //copy.setParas(this.getParas());
        //copy.parseParas();
        return copy;
    }

    public PreparedParaProducer getPreparedParaProducer() {
        return preparedParaProducer;
    }

    public void setPreparedParaProducer(PreparedParaProducer preparedParaProducer) {
        this.preparedParaProducer = preparedParaProducer;
    }

    public double getSucrate() {
        return sucrate;
    }

    public void setSucrate(double sucrate) {
        this.sucrate = sucrate;
    }

    public void addAcceptErrorCodes(int errorCode) {
        this.acceptErrorCodes.add(errorCode);
    }
    
    public boolean isAcceptableError(int errorCode){
        if(acceptErrorCodes.size() == 0) {
            return false;
        }
        
        for(int i = 0; i < acceptErrorCodes.size() ; i++){
            if(errorCode == acceptErrorCodes.get(i))
                return true;
        }
        
        return false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public static void main(String[] args){
        int i = 0;
        System.out.println("i = "+(++i));
        System.out.println("i = "+(i++));
    }

}
